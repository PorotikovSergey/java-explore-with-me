package ru.practicum.explore.mapping;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.Hit;
import ru.practicum.explore.dto.*;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.exceptions.ServerException;
import ru.practicum.explore.model.*;
import ru.practicum.explore.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventMapping {
    private final EventService eventService;
    private final Mapper mapper;

    private final FromMainToStatsClient fromMainToStatsClient;

    public List<EventFullDto> getEventsAdmin(List<Long> users, List<String> states, List<Long> categories,
                                             String rangeStart, String rangeEnd, Integer from, Integer size) {
        AdminSearchedParams params = new AdminSearchedParams(users, states, categories, rangeStart, rangeEnd);

        List<Event> list = eventService.getEventsAdmin(params, from, size);

        if (list.isEmpty()) {
            throw new NotFoundException("Список событий по данным критериям пуст");
        }

        return list.stream().map(mapper::fromEventToFullDto).collect(Collectors.toList());
    }

    public EventFullDto putEventAdmin(long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = mapper.fromAdminUpdateEventRequestToEvent(adminUpdateEventRequest);

        Event backEvent = eventService.putEventAdmin(eventId, event);

        return mapper.fromEventToFullDto(backEvent);
    }

    public EventFullDto publishEventAdmin(long eventId) {

        Event backEvent = eventService.publishEventAdmin(eventId);

        return mapper.fromEventToFullDto(backEvent);
    }

    public EventFullDto rejectEventAdmin(long eventId) {

        Event backEvent = eventService.rejectEventAdmin(eventId);

        return mapper.fromEventToFullDto(backEvent);
    }

    public List<EventShortDto> getEventsPrivate(long userId, Integer from, Integer size) {

        List<Event> list = eventService.getEventsPrivate(userId, from, size);

        if (list.isEmpty()) {
            throw new NotFoundException("Список событий пуст");
        }

        return list.stream().map(mapper::fromEventToShortDto).collect(Collectors.toList());
    }

    public EventFullDto patchEventPrivate(long userId, UpdateEventRequest updateEventRequest) {
        Event event = mapper.fromUpdateEventRequestToEvent(updateEventRequest);

        Event backEvent = eventService.patchEventPrivate(userId, event);

        return mapper.fromEventToFullDto(backEvent);
    }

    public EventFullDto postEventPrivate(long userId, NewEventDto newEventDto) {
        Event event = mapper.fromNewDtoToEvent(newEventDto);

        Event backEvent = eventService.postEventPrivate(userId, event);

        return mapper.fromEventToFullDto(backEvent);
    }

    public EventFullDto getFullEventByIdPrivate(long userId, long eventId) {

        Event event = eventService.getFullEventByIdPrivate(userId, eventId);

        return mapper.fromEventToFullDto(event);
    }

    public EventFullDto cancelEventPrivate(long userId, long eventId) {

        Event event = eventService.cancelEventPrivate(userId, eventId);

        return mapper.fromEventToFullDto(event);
    }

    public List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid,
                                               String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                               String sort, Integer from, Integer size, HttpServletRequest request) {

        sendToStats(request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().toString());

        FilterSearchedParams params = new FilterSearchedParams(categories, paid, onlyAvailable,
                rangeStart, rangeEnd, sort, text);

        List<Event> list = eventService.getEventsPublic(params, from, size);

        if (list.isEmpty()) {
            throw new NotFoundException("Список событий по данным параметрам пуст");
        }

        return list.stream().map(mapper::fromEventToShortDto).collect(Collectors.toList());
    }

    public EventFullDto getEventByIdPublic(HttpServletRequest request, long id) {

        sendToStats(request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().toString());
        Long views = getFromStats(id);

        Event event = eventService.getEventByIdPublic(id, views);

        return mapper.fromEventToFullDto(event);
    }


    private Long getFromStats(long id) {
        Long views;
        try {
            String uri = "/events/" + id;
            List<String> uriList = new ArrayList<>();
            uriList.add(uri);
            String st = fromMainToStatsClient.getStats
                    (uriList, false, "1900-01-01 12:00:00", "2100-01-01 12:00:00").getBody().toString();
            log.warn("Вот что получили из events/" + id + " - " + st);
            views = Long.parseLong(st.substring(st.lastIndexOf("=") + 1, st.length() - 2));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServerException("Обращение к сервису статистики неудачно. " + e.getMessage());
        }
        return views;
    }

    private Long getFromStats() {
        Long views;
        try {
            String uri = "/events";
            List<String> uriList = new ArrayList<>();
            uriList.add(uri);
            String st = fromMainToStatsClient.getStats
                    (uriList, false, "1900-01-01 12:00:00", "2100-01-01 12:00:00").getBody().toString();
            log.warn("Вот что получили из /events - " + st);
            views = Long.parseLong(st.substring(st.lastIndexOf("=") + 1, st.length() - 2));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServerException("Обращение к сервису статистики неудачно. " + e.getMessage());
        }
        return views;
    }

    private void sendToStats(String url, String ip, String date) {
        try {
            Hit hit = new Hit(0, url, ip, date);
            log.warn("такой хит записался в бд статистики: " + fromMainToStatsClient.postHit(hit));
        } catch (Exception e) {
            log.error("отправка в бд не удалась");
            throw new ServerException("Отправка в бд не удалась");
        }
    }
}
