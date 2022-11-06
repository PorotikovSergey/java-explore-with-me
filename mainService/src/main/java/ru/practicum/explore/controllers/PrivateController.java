package ru.practicum.explore.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.*;
import ru.practicum.explore.mapping.EventMapping;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@ResponseBody
@org.springframework.web.bind.annotation.RequestMapping("/users/{userId}")
public class PrivateController {
    private final EventMapping eventMapping;
    private final ru.practicum.explore.mapping.RequestMapping requestMapping;

    @GetMapping("/events")
    public List<EventShortDto> getEvents(@PathVariable long userId,
                                         @RequestParam(name = "from", defaultValue = "0")
                                            Integer from,
                                         @RequestParam(name = "size", defaultValue = "10")
                                            Integer size) {

        return eventMapping.getEventsPrivate(userId, from, size);
    }

    @PatchMapping("/events")
    public EventFullDto patchEvent(@PathVariable long userId, @RequestBody UpdateEventRequest updateEventRequest) {

        return eventMapping.patchEventPrivate(userId, updateEventRequest);
    }

    @PostMapping("/events")
    public EventFullDto postEvent(@PathVariable long userId, @RequestBody NewEventDto newEventDto) {

        return eventMapping.postEventPrivate(userId, newEventDto);
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<Object> getFullEventById(@PathVariable long userId, @PathVariable long eventId) {

        return new ResponseEntity<>(eventMapping.getFullEventByIdPrivate(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable long userId, @PathVariable long eventId) {

        return eventMapping.cancelEventPrivate(userId, eventId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsInfForEvent(@PathVariable long userId, @PathVariable long eventId) {

        return requestMapping.getRequestsInfFOrEventPrivate(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto requestApprove(@PathVariable long userId, @PathVariable long eventId, @PathVariable long reqId) {

        return requestMapping.requestApprovePrivate(userId, eventId, reqId);
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto requestReject(@PathVariable long userId, @PathVariable long eventId, @PathVariable long reqId) {

        return requestMapping.requestRejectPrivate(userId, eventId, reqId);
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable long userId) {

        return requestMapping.getRequestsPrivate(userId);
    }

    @PostMapping("/requests")
    public ParticipationRequestDto postRequest(@PathVariable long userId,
                                              @RequestParam(name = "eventId")
                                              Long eventId) {

        return requestMapping.postRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable long userId, @PathVariable long requestId) {

        return requestMapping.cancelRequest(userId, requestId);
    }
}
