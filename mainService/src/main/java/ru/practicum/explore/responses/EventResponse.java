package ru.practicum.explore.responses;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.Mapper;
import ru.practicum.explore.apierrors.NotFoundApiError;
import ru.practicum.explore.auxiliary.FromMainToStatsClient;
import ru.practicum.explore.auxiliary.Hit;
import ru.practicum.explore.dto.*;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.model.*;
import ru.practicum.explore.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventResponse {

    private final EventService eventService;
    private final Mapper mapper;

    private final FromMainToStatsClient fromMainToStatsClient;

    public ResponseEntity<Object> getEventsAdmin(List<Long> users, List<String> states, List<Long> categories,
                                                 String rangeStart, String rangeEnd, Integer from, Integer size) {
        AdminSearchedParams params = new AdminSearchedParams(users, states, categories, rangeStart, rangeEnd);

        List<Event> list = eventService.getEventsAdmin(params, from, size);

        if (list.isEmpty()) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("events"), HttpStatus.NOT_FOUND);
        }

        List<EventFullDto> resultList = list.stream().map(mapper::fromEventToFullDto).collect(Collectors.toList());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> putEventAdmin(long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = mapper.fromAdminUpdateEventRequestToEvent(adminUpdateEventRequest);

        Event backEvent = eventService.putEventAdmin(eventId, event);

        EventFullDto eventFullDto = mapper.fromEventToFullDto(backEvent);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> publishEventAdmin(long eventId) {

        Event backEvent = eventService.publishEventAdmin(eventId);

        EventFullDto eventFullDto = mapper.fromEventToFullDto(backEvent);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> rejectEventAdmin(long eventId) {

        Event backEvent = eventService.rejectEventAdmin(eventId);

        EventFullDto eventFullDto = mapper.fromEventToFullDto(backEvent);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> getEventsPrivate(long userId, Integer from, Integer size) {

        List<Event> list = eventService.getEventsPrivate(userId, from, size);

        if (list.isEmpty()) {
            throw new NotFoundException("Список событий пуст");
        }

        List<EventShortDto> resultList = list.stream().map(mapper::fromEventToShortDto).collect(Collectors.toList());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> patchEventPrivate(long userId, UpdateEventRequest updateEventRequest) {
        Event event = mapper.fromUpdateEventRequestToEvent(updateEventRequest);

        Event backEvent = eventService.patchEventPrivate(userId, event);

        EventFullDto eventFullDto = mapper.fromEventToFullDto(backEvent);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> postEventPrivate(long userId, NewEventDto newEventDto) {
        Event event = mapper.fromNewDtoToEvent(newEventDto);

        Event backEvent = eventService.postEventPrivate(userId, event);

        EventFullDto eventFullDto = mapper.fromEventToFullDto(backEvent);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> getFullEventByIdPrivate(long userId, long eventId) {

        Event event = eventService.getFullEventByIdPrivate(userId, eventId);

        EventFullDto eventFullDto = mapper.fromEventToFullDto(event);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> cancelEventPrivate(long userId, long eventId) {

        Event event = eventService.cancelEventPrivate(userId, eventId);

        EventFullDto eventFullDto = mapper.fromEventToFullDto(event);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> getEventsPublic(String text, List<Long> categories, Boolean paid,
                                                  String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                  String sort, Integer from, Integer size, HttpServletRequest request) {

        try {
            Hit hit = new Hit(0, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().toString());
            fromMainToStatsClient.postHit(hit);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        FilterSearchedParams params = new FilterSearchedParams(categories, paid, onlyAvailable, rangeStart, rangeEnd, sort, text);
        List<Event> list = eventService.getEventsPublic(params, from, size);

        if (list.isEmpty()) {
            throw new NotFoundException("Список событий по данным параметрам пуст");
        }

        List<EventShortDto> resultList = list.stream().map(mapper::fromEventToShortDto).collect(Collectors.toList());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> getEventByIdPublic(HttpServletRequest request, long id) {

        try {
            Hit hit = new Hit(0, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().toString());
            fromMainToStatsClient.postHit(hit);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        Event event = eventService.getEventByIdPublic(id);

        EventFullDto resultEventFullDto = mapper.fromEventToFullDto(event);
        return new ResponseEntity<>(resultEventFullDto, HttpStatus.OK);
    }

}
