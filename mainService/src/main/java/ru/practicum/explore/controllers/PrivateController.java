package ru.practicum.explore.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        return eventResponse.getEventsPrivate(userId, from, size);
    }

    @PatchMapping("/events")
    public ResponseEntity<Object> patchEvent(@PathVariable long userId, @RequestBody UpdateEventRequest updateEventRequest) {
        return eventResponse.patchEventPrivate(userId, updateEventRequest);
    }

    @PostMapping("/events")
    public ResponseEntity<Object> postEvent(@PathVariable long userId, @RequestBody NewEventDto newEventDto) {
        return eventResponse.postEventPrivate(userId, newEventDto);
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<Object> getFullEventById(@PathVariable long userId, @PathVariable long eventId) {
        return eventResponse.getFullEventByIdPrivate(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public ResponseEntity<Object> cancelEvent(@PathVariable long userId, @PathVariable long eventId) {
        return eventResponse.cancelEventPrivate(userId, eventId);
    }

    @GetMapping("/events/{eventId}/requests")
    public ResponseEntity<Object> getRequestsInfForEvent(@PathVariable long userId, @PathVariable long eventId) {
        return requestResponse.getRequestsInfFOrEventPrivate(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
    public ResponseEntity<Object> requestApprove(@PathVariable long userId, @PathVariable long eventId, @PathVariable long reqId) {
        return requestResponse.requestApprovePrivate(userId, eventId, reqId);
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
    public ResponseEntity<Object> requestReject(@PathVariable long userId, @PathVariable long eventId, @PathVariable long reqId) {
        return requestResponse.requestRejectPrivate(userId, eventId, reqId);
    }

    @GetMapping("/requests")
    public ResponseEntity<Object> getRequests(@PathVariable long userId) {
        return requestResponse.getRequestsPrivate(userId);
    }

    @PostMapping("/requests")
    public ResponseEntity<Object> postRequest(@PathVariable long userId,
                                              @RequestParam(name = "eventId")
                                              Long eventId) {
        return requestResponse.postRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequest(@PathVariable long userId, @PathVariable long requestId) {
        return requestResponse.cancelRequest(userId, requestId);
    }
}
