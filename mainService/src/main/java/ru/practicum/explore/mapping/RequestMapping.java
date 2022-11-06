package ru.practicum.explore.mapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.ParticipationRequestDto;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.model.Request;
import ru.practicum.explore.service.RequestService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestMapping {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RequestService requestService;

    public List<ParticipationRequestDto> getRequestsInfFOrEventPrivate(long userId, long eventId) {

        List<Request> list = requestService.getRequestsInfFOrEventPrivate(userId, eventId);

        if (list.isEmpty()) {
            throw new NotFoundException("Список запросов на мероприятие пуст");
        }

        return list.stream()
                .map(RequestMapping::fromRequestToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    public ParticipationRequestDto requestApprovePrivate(long userId, long eventId, long reqId) {

        Request request = requestService.requestApprovePrivate(userId, eventId, reqId);

        return fromRequestToParticipationRequestDto(request);
    }

    public ParticipationRequestDto requestRejectPrivate(long userId, long eventId, long reqId) {

        Request request = requestService.requestRejectPrivate(userId, eventId, reqId);

        return fromRequestToParticipationRequestDto(request);
    }

    public List<ParticipationRequestDto> getRequestsPrivate(long userId) {

        List<Request> list = requestService.getRequestsPrivate(userId);

        if (list.isEmpty()) {
            throw new NotFoundException("Список запросов пуст");
        }

        return list.stream()
                .map(RequestMapping::fromRequestToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    public ParticipationRequestDto postRequest(long userId, long eventId) {

        Request request = requestService.postRequest(userId, eventId);

        return fromRequestToParticipationRequestDto(request);
    }

    public ParticipationRequestDto cancelRequest(long userId, long requestId) {

        Request request = requestService.cancelRequest(userId, requestId);

        return fromRequestToParticipationRequestDto(request);
    }

    public static ParticipationRequestDto fromRequestToParticipationRequestDto(Request request) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setId(request.getId());
        participationRequestDto.setRequester(request.getRequester().getId());
        participationRequestDto.setStatus(request.getStatus());
        participationRequestDto.setEvent(request.getEvent().getId());

        LocalDateTime dateTime = request.getCreateOn();
        String formattedDateTime = dateTime.format(FORMATTER);

        participationRequestDto.setCreated(formattedDateTime);

        return participationRequestDto;
    }
}
