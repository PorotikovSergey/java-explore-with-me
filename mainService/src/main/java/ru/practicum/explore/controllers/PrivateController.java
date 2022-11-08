package ru.practicum.explore.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.*;
import ru.practicum.explore.mapping.EventMapping;
import ru.practicum.explore.mapping.ReviewMapping;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class PrivateController {
    private final EventMapping eventMapping;
    private final ru.practicum.explore.mapping.RequestMapping requestMapping;
    private final ReviewMapping reviewMapping;

    //--------------------------------ФИЧА------------------------------------------
    @PostMapping("/events/{eventId}/reviews")
    public ReviewDto postReview(@PathVariable long userId,
                                @PathVariable long eventId,
                                @RequestBody NewReview newReview) {
        log.info("==ЭНДПОИНТ POST /users/{userId}/events/{eventId}/reviews");
        log.info("Приватное добавление отзыва: {}", newReview);

        return reviewMapping.postReview(userId, eventId, newReview);
    }

    @DeleteMapping("/events/{eventId}/reviews/{reviewId}")
    public void deleteReview(@PathVariable long userId,
                             @PathVariable long eventId,
                             @PathVariable long reviewId) {
        log.info("==ЭНДПОИНТ DELETE /users/{userId}/events/{eventId}/reviews/{reviewId}");
        log.info("Приватное удаление отзыва с id: {}", reviewId);

        reviewMapping.deleteReview(userId, eventId, reviewId);
    }

    @PostMapping("/events/{eventId}/reviews/{reviewId}")
    public void rateReview(@RequestParam(name = "value") @Max(10) @Min(1) Integer value,
                           @PathVariable long userId,
                           @PathVariable long eventId,
                           @PathVariable long reviewId) {
        log.info("==ЭНДПОИНТ POST /users/{userId}/events/{eventId}/reviews/{reviewId}");
        log.info("Приватное добавление оценки {} отзыву {}", value, reviewId);

        reviewMapping.rateReview(userId, eventId, reviewId, value);
    }


    //------------------------------------------------------------------------------
    @GetMapping("/events")
    public List<EventShortDto> getEvents(@PathVariable long userId,
                                         @RequestParam(name = "from", defaultValue = "0")
                                         Integer from,
                                         @RequestParam(name = "size", defaultValue = "10")
                                         Integer size) {
        log.info("==ЭНДПОИНТ GET /users/{userId}/events");
        log.info("Приватное получение событий пользователем с id = {} и пагинацией from = {} и size = {} ",
                userId, from, size);
        return eventMapping.getEventsPrivate(userId, from, size);
    }

    @PatchMapping("/events")
    public EventFullDto patchEvent(@PathVariable long userId, @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("==ЭНДПОИНТ PATCH /users/{userId}/events");
        log.info("Приватное изменение пользователем с id = {} события {}",
                userId, updateEventRequest);
        return eventMapping.patchEventPrivate(userId, updateEventRequest);
    }

    @PostMapping("/events")
    public EventFullDto postEvent(@PathVariable long userId, @RequestBody NewEventDto newEventDto) {
        log.info("==ЭНДПОИНТ POST /users/{userId}/events");
        log.info("Приватное добавление пользователем с id = {} события {}",
                userId, newEventDto);
        return eventMapping.postEventPrivate(userId, newEventDto);
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<Object> getFullEventById(@PathVariable long userId, @PathVariable long eventId) {
        log.info("==ЭНДПОИНТ GET /users/{userId}/events/{eventId}");
        log.info("Приватное получение пользователем с id = {} события с id {}",
                userId, eventId);
        return new ResponseEntity<>(eventMapping.getFullEventByIdPrivate(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable long userId, @PathVariable long eventId) {
        log.info("==ЭНДПОИНТ PATCH /users/{userId}/events/{eventId}");
        log.info("Приватная отмена пользователем с id = {} события с id{}",
                userId, eventId);
        return eventMapping.cancelEventPrivate(userId, eventId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsInfForEvent(@PathVariable long userId,
                                                                @PathVariable long eventId) {
        log.info("==ЭНДПОИНТ GET /users/{userId}/events/{eventId}/requests");
        log.info("Приватная получение пользователем с id = {} реквестов к событию с id{}",
                userId, eventId);
        return requestMapping.getRequestsInfFOrEventPrivate(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto requestApprove(@PathVariable long userId, @PathVariable long eventId,
                                                  @PathVariable long reqId) {
        log.info("==ЭНДПОИНТ PATCH /users/{userId}/events/{eventId}/requests/{reqId}/confirm");
        log.info("Приватное подтверждение пользователем с id = {} реквеста с id {} к событию с id{}",
                userId, reqId, eventId);
        return requestMapping.requestApprovePrivate(userId, eventId, reqId);
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto requestReject(@PathVariable long userId, @PathVariable long eventId,
                                                 @PathVariable long reqId) {
        log.info("==ЭНДПОИНТ PATCH /users/{userId}/events/{eventId}/requests/{reqId}/reject");
        log.info("Приватный отказ пользователем с id = {} реквеста с id {} к событию с id{}",
                userId, reqId, eventId);
        return requestMapping.requestRejectPrivate(userId, eventId, reqId);
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable long userId) {
        log.info("==ЭНДПОИНТ GET /users/{userId}/requests");
        log.info("Приватное поулчение пользователем с id = {} реквестов",
                userId);
        return requestMapping.getRequestsPrivate(userId);
    }

    @PostMapping("/requests")
    public ParticipationRequestDto postRequest(@PathVariable long userId,
                                               @RequestParam(name = "eventId")
                                               Long eventId) {
        log.info("==ЭНДПОИНТ POST /users/{userId}/requests");
        log.info("Приватное добавление пользователем с id = {} реквеста на событие с id {}",
                userId, eventId);
        return requestMapping.postRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable long userId, @PathVariable long requestId) {
        log.info("==ЭНДПОИНТ PATCH /users/{userId}/requests/{requestId}/cancel");
        log.info("Приватная отмена пользователем с id = {} реквеста с id {}",
                userId, requestId);
        return requestMapping.cancelRequest(userId, requestId);
    }
}
