package ru.practicum.explore.mapping;

import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.*;
import ru.practicum.explore.model.*;
import ru.practicum.explore.service.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Mapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventService eventService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final LocationService locationService;

    public Mapper(EventService eventService, CategoryService categoryService, UserService userService, LocationService locationService) {
        this.eventService = eventService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.locationService = locationService;
    }

//---------------------------USER------------------------------------------

    public UserDto fromUserToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public UserShortDto fromUserToShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());
        return userShortDto;
    }

    public User fromUserRequestToUser(NewUserRequest newUserRequest) {
        User user = new User();
        user.setName(newUserRequest.getName());
        user.setEmail(newUserRequest.getEmail());
        return user;
    }

//---------------------------EVENT------------------------------------------

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
        CategoryDto categoryDto = fromCategoryToDto(category);
        eventFullDto.setCategory(categoryDto);
        User initiator = userService.getUser(event.getOwner().getId());
        UserDto initiatorDto = fromUserToDto(initiator);
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
        CategoryDto categoryDto = fromCategoryToDto(category);
        eventShortDto.setCategory(categoryDto);

        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());

        eventShortDto.setEventDate(event.getEventDate().format(FORMATTER));

        User user = userService.getUser(event.getOwner().getId());
        UserShortDto initiator = fromUserToShortDto(user);
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

//---------------------------CATEGORY------------------------------------------

    public CategoryDto fromCategoryToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public Category fromDtoToCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }

    public Category fromNewDtoToCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

//---------------------------COMPILATION------------------------------------------

    public CompilationDto fromCompilationToDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.isPinned());
        if (!compilation.getEventList().isEmpty()) {
            List<EventShortDto> list = compilation.getEventList().stream().map(this::fromEventToShortDto).collect(Collectors.toList());
            compilationDto.setEvents(list);
        } else {
            compilationDto.setEvents(Collections.emptyList());
        }
        return compilationDto;
    }

    public Compilation fromNewDtoToCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.isPinned());
        if (!newCompilationDto.getEvents().isEmpty()) {
            compilation.setEventList(eventService.getAllByIds(newCompilationDto.getEvents()));
            compilation.setEventList(eventService.getAllByIds(newCompilationDto.getEvents()));
        } else {
            compilation.setEventList(new ArrayList<>());
        }
        return compilation;
    }

//---------------------------REQUEST------------------------------------------

    public ParticipationRequestDto fromRequestToParticipationRequestDto(Request request) {
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
