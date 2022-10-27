package ru.practicum.explore.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.apierrors.ServerApiError;
import ru.practicum.explore.dto.NewEventDto;
import ru.practicum.explore.model.UpdateEventRequest;
import ru.practicum.explore.responses.EventResponse;
import ru.practicum.explore.responses.RequestResponse;

@Slf4j
@RestController
@RequestMapping("/users/{userId}")
public class PrivateController {
    private final EventResponse eventResponse;
    private final RequestResponse requestResponse;

    @Autowired
    public PrivateController(EventResponse eventResponse, RequestResponse requestResponse) {
        this.eventResponse = eventResponse;
        this.requestResponse = requestResponse;
    }

    @GetMapping("/events")
    public ResponseEntity<Object> getEvents(@PathVariable long userId,
                                            @RequestParam(name = "from", defaultValue = "0")
                                            Integer from,
                                            @RequestParam(name = "size", defaultValue = "10")
                                            Integer size) {
        try {
            return eventResponse.getEventsPrivate(userId, from, size);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerApiError.getServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/events")
    public ResponseEntity<Object> patchEvent(@PathVariable long userId, @RequestBody UpdateEventRequest updateEventRequest) {
        try {
            return eventResponse.patchEventPrivate(userId, updateEventRequest);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerApiError.getServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/events")
    public ResponseEntity<Object> postEvent(@PathVariable long userId, @RequestBody NewEventDto newEventDto) {
        try {
            return eventResponse.postEventPrivate(userId, newEventDto);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerApiError.getServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<Object> getFullEventById(@PathVariable long userId, @PathVariable long eventId) {
        try {
            return eventResponse.getFullEventByIdPrivate(userId, eventId);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerApiError.getServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/events/{eventId}")
    public ResponseEntity<Object> cancelEvent(@PathVariable long userId, @PathVariable long eventId) {
        try {
            return eventResponse.cancelEventPrivate(userId, eventId);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerApiError.getServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/events/{eventId}/requests")
    public ResponseEntity<Object> getRequestsInfForEvent(@PathVariable long userId, @PathVariable long eventId) {
        try {
            return requestResponse.getRequestsInfFOrEventPrivate(userId, eventId);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerApiError.getServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
    public ResponseEntity<Object> requestApprove(@PathVariable long userId, @PathVariable long eventId, @PathVariable long reqId) {
        try {
            return requestResponse.requestApprovePrivate(userId, eventId, reqId);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerApiError.getServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
    public ResponseEntity<Object> requestReject(@PathVariable long userId, @PathVariable long eventId, @PathVariable long reqId) {
        try {
            return requestResponse.requestRejectPrivate(userId, eventId, reqId);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerApiError.getServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/requests")
    public ResponseEntity<Object> getRequests(@PathVariable long userId) {
        try {
            return requestResponse.getRequestsPrivate(userId);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerApiError.getServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/requests")
    public ResponseEntity<Object> postRequest(@PathVariable long userId,
                                              @RequestParam(name = "eventId")
                                              Long eventId) {
        try {
            return requestResponse.postRequest(userId, eventId);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerApiError.getServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequest(@PathVariable long userId, @PathVariable long requestId) {
        try {
            return requestResponse.cancelRequest(userId, requestId);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerApiError.getServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
