package ru.practicum.explore;

import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.*;
import ru.practicum.explore.model.*;
import ru.practicum.explore.service.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class Mapper {

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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = event.getEventDate();
        String formattedDateTime = dateTime.format(formatter);
        eventFullDto.setEventDate(formattedDateTime);

        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setRequestModeration(event.isRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setViews(event.getViews());

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime1 = event.getCreatedOn();
        String formattedDateTime1 = dateTime1.format(formatter1);
        eventFullDto.setCreatedOn(formattedDateTime1);

        Category category = categoryService.getCategoryByIdPublic(event.getCategoryId());
        CategoryDto categoryDto = fromCategoryToDto(category);
        eventFullDto.setCategory(categoryDto);
        User initiator = userService.getUser(event.getOwnerId());
        UserDto initiatorDto = fromUserToDto(initiator);
        eventFullDto.setInitiator(initiatorDto);
        Location location = locationService.getLocation(event.getLocationId());
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

        Category category = categoryService.getCategoryByIdPublic(event.getCategoryId());
        CategoryDto categoryDto = fromCategoryToDto(category);
        eventShortDto.setCategory(categoryDto);

        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = event.getEventDate();
        String formattedDateTime = dateTime.format(formatter);
        eventShortDto.setEventDate(formattedDateTime);

        User user = userService.getUser(event.getOwnerId());
        UserShortDto initiator = fromUserToShortDto(user);
        eventShortDto.setInitiator(initiator);

        eventShortDto.setPaid(event.getPaid());
        return eventShortDto;
    }

    public Event fromUpdateEventRequestToEvent(UpdateEventRequest updateEventRequest) {
        Event event = new Event();
        event.setAnnotation(updateEventRequest.getAnnotation());
        event.setCategoryId(updateEventRequest.getCategory());
        event.setDescription(updateEventRequest.getDescription());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        event.setEventDate(LocalDateTime.parse(updateEventRequest.getEventDate(), formatter));

        event.setId(updateEventRequest.getEventId());
        event.setPaid(updateEventRequest.getPaid());
        event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        event.setTitle(updateEventRequest.getTitle());
        return event;
    }

    public Event fromAdminUpdateEventRequestToEvent(AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = new Event();
        event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        event.setCategoryId(adminUpdateEventRequest.getCategory());
        event.setDescription(adminUpdateEventRequest.getDescription());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        event.setEventDate(LocalDateTime.parse(adminUpdateEventRequest.getEventDate(), formatter));

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
        event.setCategoryId(newEventDto.getCategory());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), formatter));

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

    public NewCategoryDto fromCategoryToNewDto(Category category) {
        NewCategoryDto newCategoryDto = new NewCategoryDto();
        newCategoryDto.setId(category.getId());
        newCategoryDto.setName(category.getName());
        return newCategoryDto;
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
        if(!compilation.getEvents().isBlank()) {
            List<Long> ids = fromStringToListOfLongs(compilation.getEvents().trim());
            for (Long id : ids) {
                Event event;
                if(id==0) {
                    event = new Event();
                } else {
                    event = eventService.getEventById(id);
                }
                EventShortDto eventShortDto = fromEventToShortDto(event);
                compilationDto.getEvents().add(eventShortDto);
            }
        } else {
            compilationDto.setEvents(Collections.emptyList());
        }
        return compilationDto;
    }

    public Compilation fromNewDtoToCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.isPinned());
        if(!newCompilationDto.getEvents().isEmpty()) {
            compilation.setEvents(fromListOfLongsToString(newCompilationDto.getEvents()));
        } else {
            compilation.setEvents("");
        }
        return compilation;
    }

//---------------------------REQUEST------------------------------------------

    public ParticipationRequestDto fromRequestToParticipationRequestDto(Request request) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setId(request.getId());
        participationRequestDto.setRequester(request.getRequester());
        participationRequestDto.setStatus(request.getStatus());
        participationRequestDto.setEvent(request.getEvent());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime dateTime = request.getCreateOn();
        String formattedDateTime = dateTime.format(formatter);

        participationRequestDto.setCreated(formattedDateTime);

        return participationRequestDto;
    }



//-----------------------------------------------------------------------------------------

    private String fromListOfLongsToString(List<Long> list) {
        StringBuilder sb = new StringBuilder();
        for (Long number : list) {
            sb.append(number);
            sb.append(" ");
        }
        return sb.toString();
    }

    private List<Long> fromStringToListOfLongs(String str) {
        List<Long> list = new ArrayList<>();
        String[] array = str.split(" ");
        for (String letter : array) {
            list.add(Long.parseLong(letter));
        }
        return list;
    }

}
