package ru.practicum.explore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.Request;
import ru.practicum.explore.model.RequestStatus;
import ru.practicum.explore.storage.EventRepository;
import ru.practicum.explore.storage.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository, EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
    }

    public List<Request> getRequestsInfFOrEventPrivate(long userId, long eventId) {
        List<Request> list = requestRepository.findAllByEventAndOwnerId(eventId, userId);
        return list;
    }

    public Request requestApprovePrivate(long userId, long eventId, long reqId) {
        Request request = requestRepository.findRequestByEventAndOwnerIdAndId(eventId, userId, reqId);
        if (request == null) {
            return null;
        }
        Event event = eventRepository.findEventByIdAndOwnerId(eventId, userId);
        if (event == null) {
            return null;
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
            List<Request> list = requestRepository.findAllByEventAndOwnerId(eventId, userId);
            for (Request req : list) {
                req.setStatus(RequestStatus.REJECTED.toString());
            }
        }
        return request;
    }

    public Request requestRejectPrivate(long userId, long eventId, long reqId) {
        Request request = requestRepository.findRequestByEventAndOwnerIdAndId(eventId, userId, reqId);
        if (request == null) {
            return null;
        }
        Event event = eventRepository.findEventByIdAndOwnerId(eventId, userId);
        if (event == null) {
            return null;
        }
        request.setStatus(RequestStatus.REJECTED.toString());
        event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        requestRepository.save(request);
        return request;

    }

    public List<Request> getRequestsPrivate(long userId) {
        List<Request> list = requestRepository.findAllByRequester(userId);
        return list;
    }

    public Request postRequest(long userId, long eventId) {
        Request checkIfExistRequest = requestRepository.findRequestByEventAndRequester(eventId, userId);
        if (checkIfExistRequest != null) {
            return null;
        }
        Event event = eventRepository.findById(eventId).get();
        if (event == null) {
            return null;
        }
        if (event.getOwnerId() == userId) {
            return null;
        }
        if (event.getPublishedOn()==null) {
            return null;
        }
        List<Request> list = requestRepository.findAllByEvent(eventId);
        if (list.size() >= event.getParticipantLimit()) {
            return null;
        }
        Request request = new Request();
        request.setRequester(userId);
        request.setEvent(eventId);

        request.setCreateOn(LocalDateTime.now());

        request.setOwnerId(event.getOwnerId());
        request.setRequester(userId);
        if (!event.isRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED.toString());
        } else {
            request.setStatus(RequestStatus.PENDING.toString());
        }
        requestRepository.save(request);
        return request;
    }

    public Request cancelRequest(long userId, long requestId) {
        Request request = requestRepository.findRequestByIdAndRequester(requestId, userId);
        if(request==null) {
            return null;
        }
        requestRepository.delete(request);
        request.setStatus(RequestStatus.CANCELED.toString());
        return request;
    }
}
