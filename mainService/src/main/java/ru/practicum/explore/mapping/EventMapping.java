package ru.practicum.explore.mapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.Hit;
import ru.practicum.explore.dto.*;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.exceptions.ServerException;
import ru.practicum.explore.model.*;
import ru.practicum.explore.service.CategoryService;
import ru.practicum.explore.service.EventService;
import ru.practicum.explore.service.LocationService;
import ru.practicum.explore.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventMapping {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventService eventService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final LocationService locationService;

    private final FromMainToStatsClient fromMainToStatsClient;

    //----------------------мапперы-------------------------------------------------------------------

    public EventFullDto fromEventToFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setDescription(event.getDescription());

        eventFullDto.setEventDate(event.getEventDate().format(FORMATTER));

        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setRequestModeration(event.isRequestModeration());
        eventFullDto.setState(event.getState());

        if (event.getPublishedOn() != null) {
            eventFullDto.setPublishedOn(event.getPublishedOn().format(FORMATTER));
        }
        eventFullDto.setViews(event.getViews());

        eventFullDto.setCreatedOn(event.getCreatedOn().format(FORMATTER));

        Category category = categoryService.getCategoryByIdPublic(event.getCategory().getId());
        CategoryDto categoryDto = CategoryMapping.fromCategoryToDto(category);
        eventFullDto.setCategory(categoryDto);
        User initiator = userService.getUser(event.getOwner().getId());
        UserDto initiatorDto = UserMapping.fromUserToDto(initiator);
        eventFullDto.setInitiator(initiatorDto);
        Location location = locationService.getLocation(event.getLocation().getId());
        eventFullDto.setLocation(location);
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        return eventFullDto;
    }

    public EventShortDto fromEventToShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setAnnotation(event.getAnnotation());

        Category category = categoryService.getCategoryByIdPublic(event.getCategory().getId());
        CategoryDto categoryDto = CategoryMapping.fromCategoryToDto(category);
        eventShortDto.setCategory(categoryDto);

        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());

        eventShortDto.setEventDate(event.getEventDate().format(FORMATTER));

        User user = userService.getUser(event.getOwner().getId());
        UserShortDto initiator = UserMapping.fromUserToShortDto(user);
        eventShortDto.setInitiator(initiator);

        eventShortDto.setPaid(event.getPaid());
        return eventShortDto;
    }

    public Event fromUpdateEventRequestToEvent(UpdateEventRequest updateEventRequest) {
        Event event = new Event();
        event.setAnnotation(updateEventRequest.getAnnotation());
        event.setCategory(categoryService.getCategoryByIdPublic(updateEventRequest.getCategory()));
        event.setDescription(updateEventRequest.getDescription());

        event.setEventDate(LocalDateTime.parse(updateEventRequest.getEventDate(), FORMATTER));

        event.setId(updateEventRequest.getEventId());
        event.setPaid(updateEventRequest.getPaid());
        event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        event.setTitle(updateEventRequest.getTitle());
        return event;
    }

    public Event fromAdminUpdateEventRequestToEvent(AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = new Event();
        event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        event.setCategory(categoryService.getCategoryByIdPublic(adminUpdateEventRequest.getCategory()));
        event.setDescription(adminUpdateEventRequest.getDescription());

        event.setEventDate(LocalDateTime.parse(adminUpdateEventRequest.getEventDate(), FORMATTER));

        event.setPaid(adminUpdateEventRequest.getPaid());
        event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        event.setTitle(adminUpdateEventRequest.getTitle());
        event.setLocation(adminUpdateEventRequest.getLocation());
        event.setRequestModeration(adminUpdateEventRequest.isRequestModeration());
        return event;
    }

    public Event fromNewDtoToEvent(NewEventDto newEventDto) {
        Event event = new Event();
        event.setTitle(newEventDto.getTitle());
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setCategory(categoryService.getCategoryByIdPublic(newEventDto.getCategory()));

        event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER));

        Location location = new Location();
        location.setLat(newEventDto.getLocation().getLat());
        location.setLon(newEventDto.getLocation().getLon());
        event.setLocation(location);
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.isRequestModeration());
        return event;
    }

    //----------------------------------------------------------------------------------------------

    public List<EventFullDto> getEventsAdmin(List<Long> users, List<String> states, List<Long> categories,
                                             String rangeStart, String rangeEnd, Integer from, Integer size) {

        AdminSearchedParams params = fromParamsToObject(users, states, categories, rangeStart, rangeEnd);

        List<Event> list = eventService.getEventsAdmin(params, from, size);

        if (list.isEmpty()) {
            throw new NotFoundException("Список событий по данным критериям пуст");
        }

        return list.stream().map(this::fromEventToFullDto).collect(Collectors.toList());
    }

    public EventFullDto putEventAdmin(long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = fromAdminUpdateEventRequestToEvent(adminUpdateEventRequest);

        Event backEvent = eventService.putEventAdmin(eventId, event);

        return fromEventToFullDto(backEvent);
    }

    public EventFullDto publishEventAdmin(long eventId) {

        Event backEvent = eventService.publishEventAdmin(eventId);

        return fromEventToFullDto(backEvent);
    }

    public EventFullDto rejectEventAdmin(long eventId) {

        Event backEvent = eventService.rejectEventAdmin(eventId);

        return fromEventToFullDto(backEvent);
    }

    public List<EventShortDto> getEventsPrivate(long userId, Integer from, Integer size) {

        List<Event> list = eventService.getEventsPrivate(userId, from, size);

        if (list.isEmpty()) {
            throw new NotFoundException("Список событий пуст");
        }

        return list.stream().map(this::fromEventToShortDto).collect(Collectors.toList());
    }

    public EventFullDto patchEventPrivate(long userId, UpdateEventRequest updateEventRequest) {
        Event event = fromUpdateEventRequestToEvent(updateEventRequest);

        Event backEvent = eventService.patchEventPrivate(userId, event);

        return fromEventToFullDto(backEvent);
    }

    public EventFullDto postEventPrivate(long userId, NewEventDto newEventDto) {
        Event event = fromNewDtoToEvent(newEventDto);

        Event backEvent = eventService.postEventPrivate(userId, event);

        return fromEventToFullDto(backEvent);
    }

    public EventFullDto getFullEventByIdPrivate(long userId, long eventId) {

        Event event = eventService.getFullEventByIdPrivate(userId, eventId);

        return fromEventToFullDto(event);
    }

    public EventFullDto cancelEventPrivate(long userId, long eventId) {

        Event event = eventService.cancelEventPrivate(userId, eventId);

        return fromEventToFullDto(event);
    }

    public List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid,
                                               String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                               String sort, Integer from, Integer size, HttpServletRequest request) {

        FilterSearchedParams params = produceFilterParams(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort);

        System.out.println("params :" + params);

        List<Event> list;

        try {
            log.debug("Отправляем запрос в сервис статистики");
            log.info("uri = {}, ip = {}, time = {}",
                    request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().toString());
            updateStatsOfEvent(request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().toString());
        } catch (Exception e) {
            log.error("Отправка статистики не удалась");
        }

        list = eventService.getEventsPublic(params, from, size);

        if (list.isEmpty()) {
            throw new NotFoundException("Список событий по данным параметрам пуст");
        }

        return list.stream().map(this::fromEventToShortDto).collect(Collectors.toList());
    }

    public EventFullDto getEventByIdPublic(HttpServletRequest request, long id) {

        Event event;
        long views = 0L;

        try {
            log.debug("Отправляем запрос в сервис статистики");
            log.info("uri = {}, ip = {}, time = {}",
                    request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().toString());
            views = updateStatsOfEvent("/events/" + id, request.getRemoteAddr(), LocalDateTime.now().toString());
        } catch (Exception e) {
            log.error("Отправка в и получение из статистики не удалась");
        }
        event = eventService.getEventByIdPublic(id, views);
        return fromEventToFullDto(event);
    }

    private Long updateStatsOfEvent(String url, String ip, String date) {
        try {
            Hit hit = new Hit(0, url, ip, date);
            long views = Long.parseLong(fromMainToStatsClient.updateStats(hit).getBody().toString());
            log.warn("такое кол-во просмотров у данного данного урла " + url + " : " + views);
            return views;
        } catch (Exception e) {
            log.error("отправка в бд не удалась");
            throw new ServerException("Отправка в бд не удалась");
        }
    }

    private AdminSearchedParams fromParamsToObject(List<Long> users, List<String> states, List<Long> categories,
                                                   String rangeStart, String rangeEnd) {
        AdminSearchedParams params = new AdminSearchedParams();
        if (users != null && !users.isEmpty()) {
            params.setUsers(users);
        }
        if (states != null && !states.isEmpty()) {
            params.setStates(states);
        }
        if (categories != null && !categories.isEmpty()) {
            params.setCategories(categories);
        }
        params.setRangeStart(LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        params.setRangeEnd(LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return params;
    }

    private FilterSearchedParams produceFilterParams(String text, List<Long> categories, Boolean paid,
                                                     String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                     String sort) {
        FilterSearchedParams params = new FilterSearchedParams();
        if (text != null && !text.isEmpty()) {
            params.setText(text);
        }
        if (categories != null && !categories.isEmpty()) {
            params.setCategories(categories);
        }
        if(onlyAvailable!=null && onlyAvailable) {
            params.setOnlyAvailable(true);
        }
        if(paid!=null) {
            params.setOnlyAvailable(paid);
        }
        params.setSort(sort);
        params.setRangeStart(LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        params.setRangeEnd(LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return params;
    }
}
