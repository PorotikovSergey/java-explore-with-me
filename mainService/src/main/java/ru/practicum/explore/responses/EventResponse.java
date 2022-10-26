package ru.practicum.explore.responses;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.Mapper;
import ru.practicum.explore.dto.EventFullDto;
import ru.practicum.explore.dto.EventShortDto;
import ru.practicum.explore.dto.NewEventDto;
import ru.practicum.explore.exceptions.ValidationException;
import ru.practicum.explore.model.*;
import ru.practicum.explore.service.EventService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventResponse {

    private final EventService eventService;
    private final Mapper mapper;

    @Autowired
    public EventResponse(EventService eventService, Mapper mapper) {
        this.eventService = eventService;
        this.mapper = mapper;
    }

    public ResponseEntity<Object> getEventsAdmin(List<Long> users, List<String> states, List<Long> categories,
                                                 String rangeStart, String rangeEnd, Integer from, Integer size) {
        AdminSearchedParams params = new AdminSearchedParams(users, states, categories, rangeStart, rangeEnd);
        List<Event> list = eventService.getEventsAdmin(params, from, size);
        if (list.isEmpty()) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        List<EventFullDto> resultList = list.stream().map(mapper::fromEventToFullDto).collect(Collectors.toList());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> putEventAdmin(long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = mapper.fromAdminUpdateEventRequestToEvent(adminUpdateEventRequest);
        Event backEvent = eventService.putEventAdmin(eventId, event);
        if (event == null) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        EventFullDto eventFullDto = mapper.fromEventToFullDto(backEvent);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> publishEventAdmin(long eventId) {
        Event backEvent = eventService.publishEventAdmin(eventId);
        if (backEvent == null) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        EventFullDto eventFullDto = mapper.fromEventToFullDto(backEvent);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> rejectEventAdmin(long eventId) {
        Event backEvent = eventService.rejectEventAdmin(eventId);
        if (backEvent == null) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        EventFullDto eventFullDto = mapper.fromEventToFullDto(backEvent);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> getEventsPrivate(long userId, Integer from, Integer size) {
        List<Event> list = eventService.getEventsPrivate(userId, from, size);
        if (list.isEmpty()) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
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
        if (event == null) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        EventFullDto eventFullDto = mapper.fromEventToFullDto(event);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> cancelEventPrivate(long userId, long eventId) {
        Event event = eventService.cancelEventPrivate(userId, eventId);
        if (event == null) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
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
            ApiError apiError = new ApiError();
            apiError.setStatus("ERROR_RESPONSE");
            apiError.setReason("The response is bad");
            apiError.setMessage("Rewrite your response.");
            apiError.setTimestamp(LocalDateTime.now().toString());
            return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
        }
        if (list.isEmpty()) {
            ApiError apiError = new ApiError();
            apiError.setStatus("NOT_FOUND");
            apiError.setReason("The required objects were not found.");
            apiError.setMessage("Events were not found.");
            apiError.setTimestamp(LocalDateTime.now().toString());
            return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
        }
        List<EventShortDto> resultList = list.stream().map(mapper::fromEventToShortDto).collect(Collectors.toList());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> getEventByIdPublic(long id) {
        Event event;
        try {
            event = eventService.getEventByIdPublic(id);
        } catch (ValidationException e) {
            ApiError apiError = new ApiError();
            apiError.setStatus("FORBIDDEN");
            apiError.setReason("Allowed only published events");
            apiError.setMessage(e.getMessage());
            apiError.setTimestamp(LocalDateTime.now().toString());
            return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
        }
        if (event == null) {
            ApiError apiError = new ApiError();
            apiError.setStatus("NOT_FOUND");
            apiError.setReason("The required object was not found.");
            apiError.setMessage("Event was not found.");
            apiError.setTimestamp(LocalDateTime.now().toString());
            return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
        }
        EventFullDto resultEventFullDto = mapper.fromEventToFullDto(event);
        return new ResponseEntity<>(resultEventFullDto, HttpStatus.OK);
    }

}
