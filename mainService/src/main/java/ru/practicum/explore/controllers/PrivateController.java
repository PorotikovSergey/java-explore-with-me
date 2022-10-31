package ru.practicum.explore.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.NewEventDto;
import ru.practicum.explore.dto.UpdateEventRequest;
import ru.practicum.explore.responses.EventResponse;
import ru.practicum.explore.responses.RequestResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class PrivateController {
    private final EventResponse eventResponse;
    private final RequestResponse requestResponse;

    @GetMapping("/events")
    public ResponseEntity<Object> getEvents(@PathVariable long userId,
                                            @RequestParam(name = "from", defaultValue = "0")
                                            Integer from,
                                            @RequestParam(name = "size", defaultValue = "10")
                                            Integer size) {

        return new ResponseEntity<>(eventResponse.getEventsPrivate(userId, from, size), HttpStatus.OK);
    }

    @PatchMapping("/events")
    public ResponseEntity<Object> patchEvent(@PathVariable long userId, @RequestBody UpdateEventRequest updateEventRequest) {

        return new ResponseEntity<>(eventResponse.patchEventPrivate(userId, updateEventRequest), HttpStatus.OK);
    }

    @PostMapping("/events")
    public ResponseEntity<Object> postEvent(@PathVariable long userId, @RequestBody NewEventDto newEventDto) {

        return new ResponseEntity<>(eventResponse.postEventPrivate(userId, newEventDto), HttpStatus.OK);
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<Object> getFullEventById(@PathVariable long userId, @PathVariable long eventId) {

        return new ResponseEntity<>(eventResponse.getFullEventByIdPrivate(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/events/{eventId}")
    public ResponseEntity<Object> cancelEvent(@PathVariable long userId, @PathVariable long eventId) {

        return new ResponseEntity<>(eventResponse.cancelEventPrivate(userId, eventId), HttpStatus.OK);
    }

    @GetMapping("/events/{eventId}/requests")
    public ResponseEntity<Object> getRequestsInfForEvent(@PathVariable long userId, @PathVariable long eventId) {

        return new ResponseEntity<>(requestResponse.getRequestsInfFOrEventPrivate(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
    public ResponseEntity<Object> requestApprove(@PathVariable long userId, @PathVariable long eventId, @PathVariable long reqId) {

        return new ResponseEntity<>(requestResponse.requestApprovePrivate(userId, eventId, reqId), HttpStatus.OK);
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
    public ResponseEntity<Object> requestReject(@PathVariable long userId, @PathVariable long eventId, @PathVariable long reqId) {

        return new ResponseEntity<>(requestResponse.requestRejectPrivate(userId, eventId, reqId), HttpStatus.OK);
    }

    @GetMapping("/requests")
    public ResponseEntity<Object> getRequests(@PathVariable long userId) {

        return new ResponseEntity<>(requestResponse.getRequestsPrivate(userId), HttpStatus.OK);
    }

    @PostMapping("/requests")
    public ResponseEntity<Object> postRequest(@PathVariable long userId,
                                              @RequestParam(name = "eventId")
                                              Long eventId) {

        return new ResponseEntity<>(requestResponse.postRequest(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequest(@PathVariable long userId, @PathVariable long requestId) {

        return new ResponseEntity<>(requestResponse.cancelRequest(userId, requestId), HttpStatus.OK);
    }
}
