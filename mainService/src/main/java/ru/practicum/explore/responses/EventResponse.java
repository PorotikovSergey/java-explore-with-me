package ru.practicum.explore.responses;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.Mapper;
import ru.practicum.explore.apierrors.ForbiddenError;
import ru.practicum.explore.apierrors.NotFoundApiError;
import ru.practicum.explore.dto.*;
import ru.practicum.explore.exceptions.ValidationException;
import ru.practicum.explore.model.*;
import ru.practicum.explore.service.EventService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventResponse {

    private final EventService eventService;
    private final Mapper mapper;

    public ResponseEntity<Object> getEventsAdmin(List<Long> users, List<String> states, List<Long> categories,
                                                 String rangeStart, String rangeEnd, Integer from, Integer size) {
        AdminSearchedParams params = new AdminSearchedParams(users, states, categories, rangeStart, rangeEnd);
        List<Event> list;
        try {
            list = eventService.getEventsAdmin(params, from, size);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("events"), HttpStatus.FORBIDDEN);
        }

        if (list.isEmpty()) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("events"), HttpStatus.NOT_FOUND);
        }

        List<EventFullDto> resultList = list.stream().map(mapper::fromEventToFullDto).collect(Collectors.toList());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> putEventAdmin(long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = mapper.fromAdminUpdateEventRequestToEvent(adminUpdateEventRequest);
        Event backEvent;
        try {
            backEvent = eventService.putEventAdmin(eventId, event);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("event"), HttpStatus.FORBIDDEN);
        }

        if (event == null) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("event"), HttpStatus.NOT_FOUND);
        }
        EventFullDto eventFullDto = mapper.fromEventToFullDto(backEvent);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> publishEventAdmin(long eventId) {
        Event backEvent;
        try {
            backEvent = eventService.publishEventAdmin(eventId);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("event"), HttpStatus.FORBIDDEN);
        }

        if (backEvent == null) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("event"), HttpStatus.NOT_FOUND);
        }

        EventFullDto eventFullDto = mapper.fromEventToFullDto(backEvent);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> rejectEventAdmin(long eventId) {
        Event backEvent;
        try {
            backEvent = eventService.rejectEventAdmin(eventId);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("event"), HttpStatus.FORBIDDEN);
        }

        if (backEvent == null) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("events"), HttpStatus.NOT_FOUND);
        }

        EventFullDto eventFullDto = mapper.fromEventToFullDto(backEvent);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> getEventsPrivate(long userId, Integer from, Integer size) {
        List<Event> list;
        try {
            list = eventService.getEventsPrivate(userId, from, size);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("events"), HttpStatus.FORBIDDEN);
        }

        if (list.isEmpty()) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("events"), HttpStatus.NOT_FOUND);
        }

        List<EventShortDto> resultList = list.stream().map(mapper::fromEventToShortDto).collect(Collectors.toList());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> patchEventPrivate(long userId, UpdateEventRequest updateEventRequest) {
        Event event = mapper.fromUpdateEventRequestToEvent(updateEventRequest);
        Event backEvent;
        try {
            backEvent = eventService.patchEventPrivate(userId, event);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("event"), HttpStatus.FORBIDDEN);
        }

        if (backEvent == null) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("event"), HttpStatus.NOT_FOUND);
        }

        EventFullDto eventFullDto = mapper.fromEventToFullDto(backEvent);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> postEventPrivate(long userId, NewEventDto newEventDto) {
        Event event = mapper.fromNewDtoToEvent(newEventDto);
        Event backEvent;
        try {
            backEvent = eventService.postEventPrivate(userId, event);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("event"), HttpStatus.FORBIDDEN);
        }

        if (backEvent == null) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("event"), HttpStatus.NOT_FOUND);
        }

        EventFullDto eventFullDto = mapper.fromEventToFullDto(backEvent);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> getFullEventByIdPrivate(long userId, long eventId) {
        Event event;
        try {
            event = eventService.getFullEventByIdPrivate(userId, eventId);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("event"), HttpStatus.FORBIDDEN);
        }

        if (event == null) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("event"), HttpStatus.NOT_FOUND);
        }

        EventFullDto eventFullDto = mapper.fromEventToFullDto(event);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> cancelEventPrivate(long userId, long eventId) {
        Event event;
        try {
            event = eventService.cancelEventPrivate(userId, eventId);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("event"), HttpStatus.FORBIDDEN);
        }

        if (event == null) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("event"), HttpStatus.NOT_FOUND);
        }

        EventFullDto eventFullDto = mapper.fromEventToFullDto(event);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> getEventsPublic(String text, List<Long> categories, Boolean paid,
                                                  String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                  String sort, Integer from, Integer size) {

        FilterSearchedParams params;
        List<Event> list;
        try {
            params = new FilterSearchedParams(categories, paid, onlyAvailable, rangeStart, rangeEnd, sort, text);
            list = eventService.getEventsPublic(params, from, size);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("events"), HttpStatus.FORBIDDEN);
        }

        if (list.isEmpty()) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("events"), HttpStatus.NOT_FOUND);
        }

        List<EventShortDto> resultList = list.stream().map(mapper::fromEventToShortDto).collect(Collectors.toList());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> getEventByIdPublic(long id) {
        Event event;
        try {
            event = eventService.getEventByIdPublic(id);
        } catch (ValidationException e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("event"), HttpStatus.FORBIDDEN);
        }

        if (event == null) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("event"), HttpStatus.NOT_FOUND);
        }

        EventFullDto resultEventFullDto = mapper.fromEventToFullDto(event);
        return new ResponseEntity<>(resultEventFullDto, HttpStatus.OK);
    }

}
