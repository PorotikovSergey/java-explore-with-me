package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.enums.EventState;
import ru.practicum.explore.dto.AdminSearchedParams;
import ru.practicum.explore.dto.FilterSearchedParams;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.exceptions.ValidationException;
import ru.practicum.explore.model.*;
import ru.practicum.explore.storage.EventRepository;
import ru.practicum.explore.storage.UserRepository;


import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private static final String EVENT_NOT_FOUND = "События по данному id нет в базе";

    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final UserRepository userRepository;

    public List<Event> getEventsPublic(FilterSearchedParams params, Integer from, Integer size) {
        Pageable pageableDate = PageRequest.of(from, size, Sort.by("eventDate").ascending());
        Pageable pageableViews = PageRequest.of(from, size, Sort.by("views").ascending());
        Page<Event> list;
        if (params.getSort().equals("VIEWS")) {
            list = eventRepository.findByParams(params.getText().toLowerCase(), params.getCategories(),
                    params.getPaid(), params.getRangeStart(), params.getRangeEnd(),
                    params.getOnlyAvailable(), pageableViews);
        } else {
            list = eventRepository.findByParams(params.getText().toLowerCase(), params.getCategories(),
                    params.getPaid(), params.getRangeStart(), params.getRangeEnd(),
                    params.getOnlyAvailable(),pageableDate);
        }
        List<Event> result = list.getContent();
        log.info("Итоговый возвращаемый лист событий из бд такой: {}", result);
        return result;
    }

    public List<Event> getEventsPrivate(long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Event> result = eventRepository.findAllByOwnerId(userId, pageable).getContent();
        log.info("Итоговый возвращаемый лист событий из бд такой: {}", result);
        return result;
    }

    public Event getEventByIdPublic(long id, long views) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND));

        if (event.getPublishedOn() != null) {
            event.setViews(views);
            eventRepository.save(event);
            log.info("Из бд получили событие: {}", event);
            return event;
        } else {
            throw new ValidationException("Only published events can be got");
        }
    }

    public List<Event> getAllByIds(List<Long> ids) {
        List<Event> result = eventRepository.findAllById(ids);
        log.info("Итоговый возвращаемый лист событий из бд такой: {}", result);
        return result;
    }

    public Event patchEventPrivate(long userId, Event event) {
        Event eventForPatch = eventRepository.findById(event.getId())
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND));

        if (eventForPatch.getOwner().getId() != userId) {
            throw new ValidationException("Нельзя изменять чужое событие");
        }

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
            log.info("Итоговое возвращаемое событие после патча: {}", eventAfterPatch);
            return eventAfterPatch;
        } else {
            Event eventAfterPatch = patchOldEventToNew(eventForPatch, event);
            eventAfterPatch.setState(EventState.PENDING.toString());
            eventRepository.save(eventAfterPatch);
            log.info("Итоговое возвращаемое событие после патча: {}", eventAfterPatch);
            return eventAfterPatch;
        }
    }

    public Event postEventPrivate(long userId, Event event) {
        event.setOwner(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такого юзера нет")));

        LocalDateTime testDateTime = LocalDateTime.now().plusHours(2);
        if (event.getEventDate().isBefore(testDateTime)) {
            throw new ValidationException("Нельзя постить событие за 2 часа до");
        }

        event.setCreatedOn(LocalDateTime.now());
        Location location = locationService.addLocation(event.getLocation());
        event.setLocation(location);
        try {
            eventRepository.save(event);
        } catch (Exception e) {
            throw new ValidationException("Нарушение уникальности в БД события");
        }
        log.info("Итоговое сохранённое в бд событие: {}", event);
        return event;
    }

    public Event getFullEventByIdPrivate(long userId, long eventId) {
        Event event = eventRepository.findEventByIdAndOwnerId(eventId, userId);
        log.info("Итоговое возвращаемое событие: {}", event);
        return event;
    }


    public Event cancelEventPrivate(long userId, long eventId) {
        Event event = eventRepository.findEventByIdAndOwnerId(eventId, userId);
        if (event.getState().equals(EventState.PENDING.toString())) {
            event.setState(EventState.CANCELED.toString());
            eventRepository.save(event);
            log.info("Итоговое отвенённое событие: {}", event);
            return event;
        }
        throw new NotFoundException(EVENT_NOT_FOUND);
    }


    public List<Event> getEventsAdmin(AdminSearchedParams params, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Event> result = eventRepository.findByParamsAdmin(params.getUsers(), params.getStates(),
                params.getCategories(), params.getRangeStart(), params.getRangeEnd(), pageable).getContent();
        log.info("Итоговый возвращаемый лист событий из бд такой: {}", result);
        return result;
    }

    public Event putEventAdmin(long eventId, Event event) {
        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND));

        Event eventAfterPatch = patchOldEventToNew(oldEvent, event);
        eventRepository.save(eventAfterPatch);
        log.info("Итоговое возвращаемое событие: {}", eventAfterPatch);
        return eventAfterPatch;
    }

    public Event publishEventAdmin(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND));

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
        log.info("Итоговое возвращаемое опубликованное событие: {}", event);
        return event;
    }

    public Event rejectEventAdmin(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND));

        if ((event.getPublishedOn() != null)) {
            throw new ValidationException("Нельзя отменять опубликованное событие");
        }

        event.setState(EventState.CANCELED.toString());
        eventRepository.save(event);
        log.info("Итоговое возвращаемое отменённое событие: {}", event);
        return event;
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
}
