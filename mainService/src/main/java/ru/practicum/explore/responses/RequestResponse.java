package ru.practicum.explore.responses;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.Mapper;
import ru.practicum.explore.dto.ParticipationRequestDto;
import ru.practicum.explore.model.ApiError;
import ru.practicum.explore.model.Request;
import ru.practicum.explore.service.RequestService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RequestResponse {

    private final RequestService requestService;
    private final Mapper mapper;

    @Autowired
    public RequestResponse(RequestService requestService, Mapper mapper) {
        this.requestService = requestService;
        this.mapper = mapper;
    }

    public ResponseEntity<Object> getRequestsInfFOrEventPrivate(long userId, long eventId) {
        List<Request> list = requestService.getRequestsInfFOrEventPrivate(userId, eventId);
        if(list.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
        List<ParticipationRequestDto> resultList = list.stream()
                .map(mapper::fromRequestToParticipationRequestDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> requestApprovePrivate(long userId, long eventId, long reqId) {
        Request request = requestService.requestApprovePrivate(userId, eventId, reqId);
        if(request == null) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        ParticipationRequestDto participationRequestDto = mapper.fromRequestToParticipationRequestDto(request);
        return new ResponseEntity<>(participationRequestDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> requestRejectPrivate(long userId, long eventId, long reqId) {
        Request request = requestService.requestRejectPrivate(userId, eventId, reqId);
        if(request == null) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        ParticipationRequestDto participationRequestDto = mapper.fromRequestToParticipationRequestDto(request);
        return new ResponseEntity<>(participationRequestDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> getRequestsPrivate(long userId) {
        List<Request> list = requestService.getRequestsPrivate(userId);
        if(list.isEmpty()) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        List<ParticipationRequestDto> resultList = list.stream()
                .map(mapper::fromRequestToParticipationRequestDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> postRequest(long userId, long eventId) {
        Request request = requestService.postRequest(userId, eventId);
        if(request == null) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
        ParticipationRequestDto participationRequestDto = mapper.fromRequestToParticipationRequestDto(request);
        return new ResponseEntity<>(participationRequestDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> cancelRequest(long userId, long requestId) {
        Request request = requestService.cancelRequest(userId, requestId);
        if(request == null) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        ParticipationRequestDto participationRequestDto = mapper.fromRequestToParticipationRequestDto(request);
        return new ResponseEntity<>(participationRequestDto, HttpStatus.OK);
    }
}
