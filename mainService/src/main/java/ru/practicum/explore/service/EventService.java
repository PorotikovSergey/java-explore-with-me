package ru.practicum.explore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.exceptions.ValidationException;
import ru.practicum.explore.model.*;
import ru.practicum.explore.storage.EventRepository;
import ru.practicum.explore.storage.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final LocationService locationService;

    private final UserRepository userRepository;

    @Autowired
    public EventService(EventRepository eventRepository, LocationService locationService, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.locationService = locationService;
        this.userRepository = userRepository;
    }

    public List<Event> getEventsPublic(FilterSearchedParams params, Integer from, Integer size) {
        List<Event> textSearchedList = searchEventsByText(params.getText());
        List<Event> afterParamsList = getFilteredEventsFromParams(textSearchedList, params);
        List<Event> afterPageableList = getPageableList(afterParamsList, from, size);
        afterPageableList.forEach(event -> event.setViews(event.getViews() + 1));
        return afterPageableList;
    }

    public List<Event> getEventsPrivate(long userId, Integer from, Integer size) {
        List<Event> list = eventRepository.findAllByOwnerId(userId);
        return getPageableList(list, from, size);
    }

    public Event getEventByIdPublic(long id) {
        Optional<Event> optional = eventRepository.findById(id);
        if (optional.isPresent()) {
            Event backEvent = optional.get();
            if (backEvent.getPublishedOn() != null) {
                backEvent.setViews(backEvent.getViews() + 1);
                return backEvent;
            } else {
                throw new ValidationException("Only published events can be got");
            }
        }
        return null;
    }

    public Event getEventById(long id) {
        Optional<Event> optional = eventRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public List<Event> getAllByIds(List<Long> ids) {
        return eventRepository.findAllById(ids);
    }


    public Event patchEventPrivate(long userId, Event event) {
        Optional<Event> optional = eventRepository.findById(event.getId());
        if (optional.isPresent()) {
            Event eventForPatch = optional.get();
            if (eventForPatch.getOwner().getId() == userId) {
                if (eventForPatch.getState().equals(EventState.PUBLISHED.toString())) {
                    throw new ValidationException("Нельзя изменять опубликованное событие");
                }
                LocalDateTime testDateTime = LocalDateTime.now().plusHours(2);
                if (event.getEventDate().isBefore(testDateTime)) {
                    throw new ValidationException("Нельзя изменять событие за 2 часа до");
                }
                if (eventForPatch.getState().equals(EventState.PENDING.toString())) {
                    Event eventAfterPatch = patchOldEventToNew(eventForPatch, event);
                    eventRepository.save(eventAfterPatch);
                    return eventAfterPatch;
                } else {
                    Event eventAfterPatch = patchOldEventToNew(eventForPatch, event);
                    eventAfterPatch.setState(EventState.PENDING.toString());
                    eventRepository.save(eventAfterPatch);
                    return eventAfterPatch;
                }
            }
            throw new ValidationException("Нельзя изменять чужое событие");
        }
        return null;
    }

    public Event postEventPrivate(long userId, Event event) {
        event.setOwner(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого юзера нет")));
        LocalDateTime testDateTime = LocalDateTime.now().plusHours(2);
        if (event.getEventDate().isBefore(testDateTime)) {
            throw new ValidationException("Нельзя постить событие за 2 часа до");
        }
        event.setCreatedOn(LocalDateTime.now());
        Location location = locationService.addLocation(event.getLocation());
        event.setLocation(location);
        eventRepository.save(event);
        Optional<Event> optional = eventRepository.findById(event.getId());
        if (optional.isPresent()) {
            Event backEvent = optional.get();
            return backEvent;
        } else {
            return null;
        }
    }

    public Event getFullEventByIdPrivate(long userId, long eventId) {
        Event event = eventRepository.findEventByIdAndOwnerId(eventId, userId);
        return event;
    }


    public Event cancelEventPrivate(long userId, long eventId) {
        Event event = eventRepository.findEventByIdAndOwnerId(eventId, userId);
        if (event.getState().equals(EventState.PENDING.toString())) {
            event.setState(EventState.CANCELED.toString());
            eventRepository.save(event);
            return event;
        }
        return null;
    }


    public List<Event> getEventsAdmin(AdminSearchedParams params, Integer from, Integer size) {
        List<Event> list = eventRepository.findAll();
        List<Event> resultList = getEventsFromParams(list, params);
        List<Event> afterPageableList = getPageableList(resultList, from, size);
        return afterPageableList;
    }

    public Event putEventAdmin(long eventId, Event event) {
        Optional<Event> optional = eventRepository.findById(eventId);
        if (optional.isPresent()) {
            Event oldEvent = optional.get();
            Event eventAfterPatch = patchOldEventToNew(oldEvent, event);
            eventRepository.save(eventAfterPatch);
            return eventAfterPatch;
        }
        return null;
    }

    public Event publishEventAdmin(long eventId) {
        Optional<Event> optional = eventRepository.findById(eventId);
        if (optional.isPresent()) {
            Event event = optional.get();
            event.setPublishedOn(LocalDateTime.now());
            LocalDateTime testDateTime = LocalDateTime.now().plusHours(1);
            if (event.getEventDate().isBefore(testDateTime)) {
                throw new ValidationException("Нельзя публиковать событие за 1 час до");
            }
            if (!event.getState().equals(EventState.PENDING.toString())) {
                throw new ValidationException("Нельзя публиковать событие не в статусе PENDING");
            }
            event.setState(EventState.PUBLISHED.toString());
            eventRepository.save(event);
            return event;
        }
        return null;
    }

    public Event rejectEventAdmin(long eventId) {
        Optional<Event> optional = eventRepository.findById(eventId);
        if (optional.isPresent()) {
            Event event = optional.get();
            if ((event.getPublishedOn() != null)) {
                throw new ValidationException("Нельзя отменять опубликованное событие");
            }
            event.setState(EventState.CANCELED.toString());
            eventRepository.save(event);
            return event;
        }
        return null;
    }

    private Event patchOldEventToNew(Event oldEvent, Event newEvent) {
        if (!newEvent.getAnnotation().isBlank()) {
            oldEvent.setAnnotation(newEvent.getAnnotation());
        }
        if (newEvent.getCategory().getId() != 0) {
            oldEvent.setCategory(newEvent.getCategory());
        }
        if (!newEvent.getDescription().isBlank()) {
            oldEvent.setDescription(newEvent.getDescription());
        }
        if (newEvent.getPaid() != null) {
            oldEvent.setPaid(newEvent.getPaid());
        }
        if (newEvent.getParticipantLimit() > 0) {
            oldEvent.setParticipantLimit(newEvent.getParticipantLimit());
        }
        if (!newEvent.getTitle().isBlank()) {
            oldEvent.setTitle(newEvent.getTitle());
        }
        return oldEvent;
    }

    private List<Event> getEventsFromParams(List<Event> list, AdminSearchedParams params) {
        if (!params.getStates().isEmpty()) {
            for (String state : params.getStates()) {
                list = list.stream().filter(s -> s.getState().equals(state)).collect(Collectors.toList());
            }
        }
        if (!params.getUsers().isEmpty()) {
            for (Long user : params.getUsers()) {
                list = list.stream().filter(s -> s.getOwner().getId() == user).collect(Collectors.toList());
            }
        }
        if (!params.getCategories().isEmpty()) {
            for (Long category : params.getCategories()) {
                list = list.stream().filter(s -> s.getCategory().getId() == category).collect(Collectors.toList());
            }
        }
        if (!params.getStates().isEmpty()) {
            for (String state : params.getStates()) {
                list = list.stream().filter(s -> s.getState().equals(state)).collect(Collectors.toList());
            }
        }
        return list;
    }

    private List<Event> getFilteredEventsFromParams(List<Event> list, FilterSearchedParams params) {
        if (params.getOnlyAvailable()) {
            list = list.stream().filter(s -> s.getPublishedOn() != null).collect(Collectors.toList());

        }
        if (params.getPaid() != null) {
            list = list.stream().filter(s -> s.getPaid().equals(params.getPaid())).collect(Collectors.toList());
        }
        if (!params.getCategories().isEmpty()) {
            for (Long category : params.getCategories()) {
                list = list.stream().filter(s -> s.getCategory().getId() == category).collect(Collectors.toList());
            }
        }
        if (params.getRangeStart() == null) {
            list = list.stream().filter(s -> s.getEventDate().isAfter(LocalDateTime.now())).collect(Collectors.toList());
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime start = LocalDateTime.parse(params.getRangeStart(), formatter);
            LocalDateTime end = LocalDateTime.parse(params.getRangeEnd(), formatter);
            list = list.stream().filter(s -> s.getEventDate().isAfter(start)).collect(Collectors.toList());
            list = list.stream().filter(s -> s.getEventDate().isBefore(end)).collect(Collectors.toList());
        }
        if (params.getSort().equals("VIEWS")) {
            list = list.stream().sorted(Comparator.comparing(Event::getViews)).collect(Collectors.toList());
        } else {
            list = list.stream().sorted(Comparator.comparing(Event::getEventDate)).collect(Collectors.toList());
        }
        return list;
    }

    private List<Event> getPageableList(List<Event> list, int from, int size) {
        PagedListHolder<Event> page = new PagedListHolder<>(list.subList(from, list.size()));
        page.setPageSize(size);
        page.setPage(0);
        return page.getPageList();
    }

    private List<Event> searchEventsByText(String text) {
        List<Event> foundAnnotation = eventRepository
                .findAllByAnnotationContainingIgnoreCase(text);
        List<Event> foundDescription = eventRepository
                .findAllByDescriptionContainingIgnoreCase(text);
        foundAnnotation.removeAll(foundDescription);
        foundAnnotation.addAll(foundDescription);
        return foundAnnotation;
    }
}
