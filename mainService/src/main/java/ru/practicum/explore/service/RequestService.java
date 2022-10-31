package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.exceptions.ValidationException;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.Request;
import ru.practicum.explore.enums.RequestStatus;
import ru.practicum.explore.storage.EventRepository;
import ru.practicum.explore.storage.RequestRepository;
import ru.practicum.explore.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {
    private static final String REQUEST_NOT_FOUND = "Реквеста по данному id нет в базе";
    private static final String EVENT_NOT_FOUND = "События по данному id нет в базе";

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    public List<Request> getRequestsInfFOrEventPrivate(long userId, long eventId) {
        return requestRepository.findAllByEventIdAndEventOwnerId(eventId, userId);
    }

    public Request requestApprovePrivate(long userId, long eventId, long reqId) {
        Request request = requestRepository.findRequestByEventIdAndEventOwnerIdAndId(eventId, userId, reqId);
        if (request == null) {
            throw new NotFoundException(REQUEST_NOT_FOUND);
        }

        Event event = eventRepository.findEventByIdAndOwnerId(eventId, userId);
        if (event == null) {
            throw new NotFoundException(EVENT_NOT_FOUND);
        }

        if ((!event.isRequestModeration()) || (event.getParticipantLimit() == 0)) {
            request.setStatus(RequestStatus.CONFIRMED.toString());
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }

        if ((event.getParticipantLimit() > 0) && (event.getParticipantLimit() > event.getConfirmedRequests() + 1)) {
            request.setStatus(RequestStatus.CONFIRMED.toString());
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }

        if ((event.getParticipantLimit() > 0) && (event.getParticipantLimit() == event.getConfirmedRequests() + 1)) {
            request.setStatus(RequestStatus.CONFIRMED.toString());
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            List<Request> list = requestRepository.findAllByEventIdAndEventOwnerId(eventId, userId);
            for (Request req : list) {
                req.setStatus(RequestStatus.REJECTED.toString());
                requestRepository.save(req);
            }
        }

        if ((event.getParticipantLimit() > 0) && (event.getParticipantLimit() == event.getConfirmedRequests())) {
            throw new ValidationException("Достигнут предел количества заявок");
        }
        requestRepository.save(request);
        return request;
    }

    public Request requestRejectPrivate(long userId, long eventId, long reqId) {
        Request request = requestRepository.findRequestByEventIdAndEventOwnerIdAndId(eventId, userId, reqId);
        if (request == null) {
            throw new NotFoundException(REQUEST_NOT_FOUND);
        }

        Event event = eventRepository.findEventByIdAndOwnerId(eventId, userId);
        if (event == null) {
            throw new NotFoundException(EVENT_NOT_FOUND);
        }

        request.setStatus(RequestStatus.REJECTED.toString());
        event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        requestRepository.save(request);
        return request;

    }

    public List<Request> getRequestsPrivate(long userId) {
        return requestRepository.findAllByRequesterId(userId);
    }

    public Request postRequest(long userId, long eventId) {
        Request checkIfExistRequest = requestRepository.findRequestByEventIdAndRequesterId(eventId, userId);
        if (checkIfExistRequest != null) {
            throw new NotFoundException(REQUEST_NOT_FOUND);
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND));

        if (event.getOwner().getId() == userId) {
            throw new ValidationException("Нельзя публиковать запрос на своё же событие");
        }

        if (event.getPublishedOn() == null) {
            throw new ValidationException("Нельзя публиковать запрос на неопубликованное событие");
        }

        List<Request> list = requestRepository.findAllByEventId(eventId);
        if (list.size() >= event.getParticipantLimit()) {
            throw new ValidationException("Нельзя публиковать запрос на событие с исчерпаным лимитом заявок");
        }

        Request request = new Request();
        request.setRequester(userRepository.getReferenceById(userId));
        request.setEvent(eventRepository.getReferenceById(eventId));
        request.setCreateOn(LocalDateTime.now());
        request.setEventOwner(event.getOwner());

        if (!event.isRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED.toString());
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        } else {
            request.setStatus(RequestStatus.PENDING.toString());
        }

        requestRepository.save(request);
        return request;
    }

    public Request cancelRequest(long userId, long requestId) {
        Request request = requestRepository.findRequestByIdAndRequesterId(requestId, userId);
        if (request == null) {
            throw new NotFoundException(REQUEST_NOT_FOUND);
        }

        Event event = eventRepository.findById(request.getEvent().getId())
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND));
        if (request.getStatus().equals(RequestStatus.CONFIRMED.toString())) {
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        }

        requestRepository.delete(request);
        request.setStatus(RequestStatus.CANCELED.toString());
        return request;
    }
}

