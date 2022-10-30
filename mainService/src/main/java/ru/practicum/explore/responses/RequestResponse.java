package ru.practicum.explore.responses;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.Mapper;
import ru.practicum.explore.dto.ParticipationRequestDto;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.model.Request;
import ru.practicum.explore.service.RequestService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestResponse {

    private final RequestService requestService;
    private final Mapper mapper;

    public ResponseEntity<Object> getRequestsInfFOrEventPrivate(long userId, long eventId) {

        List<Request> list = requestService.getRequestsInfFOrEventPrivate(userId, eventId);

        if (list.isEmpty()) {
            throw new NotFoundException("Список запросов на мероприятие пуст");
        }

        List<ParticipationRequestDto> resultList = list.stream()
                .map(mapper::fromRequestToParticipationRequestDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> requestApprovePrivate(long userId, long eventId, long reqId) {

        Request request = requestService.requestApprovePrivate(userId, eventId, reqId);

        ParticipationRequestDto participationRequestDto = mapper.fromRequestToParticipationRequestDto(request);
        return new ResponseEntity<>(participationRequestDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> requestRejectPrivate(long userId, long eventId, long reqId) {

        Request request = requestService.requestRejectPrivate(userId, eventId, reqId);

        ParticipationRequestDto participationRequestDto = mapper.fromRequestToParticipationRequestDto(request);
        return new ResponseEntity<>(participationRequestDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> getRequestsPrivate(long userId) {

        List<Request> list = requestService.getRequestsPrivate(userId);

        if (list.isEmpty()) {
            throw new NotFoundException("Список запросов пуст");
        }

        List<ParticipationRequestDto> resultList = list.stream()
                .map(mapper::fromRequestToParticipationRequestDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> postRequest(long userId, long eventId) {

        Request request = requestService.postRequest(userId, eventId);

        ParticipationRequestDto participationRequestDto = mapper.fromRequestToParticipationRequestDto(request);
        return new ResponseEntity<>(participationRequestDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> cancelRequest(long userId, long requestId) {

        Request request = requestService.cancelRequest(userId, requestId);

        ParticipationRequestDto participationRequestDto = mapper.fromRequestToParticipationRequestDto(request);
        return new ResponseEntity<>(participationRequestDto, HttpStatus.OK);
    }
}
