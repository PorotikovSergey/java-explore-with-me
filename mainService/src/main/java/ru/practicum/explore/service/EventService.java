package ru.practicum.explore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.AdminSearchedParams;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.EventState;
import ru.practicum.explore.model.FilterSearchedParams;
import ru.practicum.explore.storage.EventRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final LocationService locationService;

    @Autowired
    public EventService(EventRepository eventRepository, LocationService locationService) {
        this.eventRepository = eventRepository;
        this.locationService = locationService;
    }

    public List<Event> getEventsPublic(FilterSearchedParams params, Integer from, Integer size) {
        List<Event> textSearchedList = searchEventsByText(params.getText());
        List<Event> afterParamsList = getFilteredEventsFromParams(textSearchedList, params);
        List<Event> afterPageableList = getPageableList(afterParamsList, from, size);
        return afterPageableList;
    }

    public List<Event> getALL() {
        return eventRepository.findAll();
    }

    public List<Event> getEventsPrivate(long userId, Integer from, Integer size) {
        List<Event> list = eventRepository.findAllByOwnerId(userId);
        List<Event> afterPageableList = getPageableList(list, from, size);
        return afterPageableList;
    }

    public Event getEventByIdPublic(long id) {
        Optional<Event> optional = eventRepository.findById(id);
        if (optional.isPresent()) {
            Event backEvent = optional.get();
            if (!backEvent.getPublishedOn().isBlank()) {
                return backEvent;
            }
        }
        return null;
    }

    public Event getEventById(long id) {
        Optional<Event> optional = eventRepository.findById(id);
        if (optional.isPresent()) {
            Event backEvent = optional.get();
            return backEvent;
        }
        return null;
    }


    public Event patchEventPrivate(long userId, Event event) {
        Optional<Event> optional = eventRepository.findById(event.getId());
        if (optional.isPresent()) {
            Event eventForPatch = optional.get();
            if (eventForPatch.getOwnerId() == userId) {
                if (eventForPatch.getState().equals(EventState.PUBLISHED.toString())) {
                    return null;
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
            return null;
        }
        return null;
    }

    public Event postEventPrivate(long userId, Event event) {
        event.setOwnerId(userId);
        event.setCreatedOn(Date.from(Instant.now()).toString());
        locationService.addLocation(event.getLocation());
        event.setLocationId(event.getLocation().getId());
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
            event.setPublishedOn(LocalDateTime.now().toString());
            if (event.getEventDate().equals(event.getPublishedOn())) {
                System.out.println("тут нужно поставить разницу минимум в час!");
                System.out.println("Да и вообще у меня даты ещё не сделаны");
                return null;
            }
            if (!event.getState().equals(EventState.PENDING.toString())) {
                return null;
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
                return null;
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
        if (newEvent.getCategoryId() != 0) {
            oldEvent.setCategoryId(newEvent.getCategoryId());
        }
        if (!newEvent.getDescription().isBlank()) {
            oldEvent.setDescription(newEvent.getDescription());
        }
        if (!newEvent.getEventDate().isBlank()) {
            oldEvent.setEventDate(newEvent.getEventDate());
        }
        if (newEvent.getPaid() != null) {     //-------вот тут нужно починить
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
                list = list.stream().filter(s -> s.getOwnerId() == user).collect(Collectors.toList());
            }
        }
        if (!params.getCategories().isEmpty()) {
            for (Long category : params.getCategories()) {
                list = list.stream().filter(s -> s.getCategoryId() == category).collect(Collectors.toList());
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
            list = list.stream().filter(s -> s.getPublishedOn()!=null).collect(Collectors.toList());

        }
        if (params.getPaid()!=null) {
                list = list.stream().filter(s -> s.getPaid().equals(params.getPaid())).collect(Collectors.toList());
        }
        if (!params.getCategories().isEmpty()) {
            for (Long category : params.getCategories()) {
                list = list.stream().filter(s -> s.getCategoryId() == category).collect(Collectors.toList());
            }
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
