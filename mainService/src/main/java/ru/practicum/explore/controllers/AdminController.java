package ru.practicum.explore.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.*;
import ru.practicum.explore.dto.AdminUpdateEventRequest;
import ru.practicum.explore.dto.NewUserRequest;
import ru.practicum.explore.mapping.CategoryMapping;
import ru.practicum.explore.mapping.CompilationMapping;
import ru.practicum.explore.mapping.EventMapping;
import ru.practicum.explore.mapping.UserMapping;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@ResponseBody
public class AdminController {

    private final UserMapping userMapping;
    private final EventMapping eventMapping;
    private final CategoryMapping categoryMapping;
    private final CompilationMapping compilationMapping;

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(name = "ids")
                                           List<Long> ids,
                                  @RequestParam(name = "from", defaultValue = "0")
                                           Integer from,
                                  @RequestParam(name = "size", defaultValue = "10")
                                           Integer size) {
        return userMapping.getUsersAdmin(ids, from, size);
    }

    @PostMapping("/users")
    public UserDto addUser(@RequestBody NewUserRequest newUserRequest) {
        return userMapping.addUserAdmin(newUserRequest);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userMapping.deleteUserAdmin(userId);
    }

    @GetMapping("/events")
    public List<EventFullDto> getEvents(@RequestParam(name = "users")
                                            List<Long> users,
                                        @RequestParam(name = "states")
                                            List<String> states,
                                        @RequestParam(name = "categories")
                                            List<Long> categories,
                                        @RequestParam(name = "rangeStart")
                                            String rangeStart,
                                        @RequestParam(name = "rangeEnd")
                                            String rangeEnd,
                                        @RequestParam(name = "from", defaultValue = "0")
                                            Integer from,
                                        @RequestParam(name = "size", defaultValue = "10")
                                            Integer size) {
        return eventMapping.getEventsAdmin(users, states, categories,
                rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/events/{eventId}")
    public EventFullDto putEvent(@PathVariable long eventId, @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        return eventMapping.putEventAdmin(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable long eventId) {
        return eventMapping.publishEventAdmin(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable long eventId) {
        return eventMapping.rejectEventAdmin(eventId);
    }

    @PatchMapping("/categories")
    public CategoryDto patchCategory(@RequestBody CategoryDto categoryDto) {
        return categoryMapping.patchCategoryAdmin(categoryDto);
    }

    @PostMapping("/categories")
    public CategoryDto postCategory(@RequestBody NewCategoryDto newCategoryDto) {
        return categoryMapping.postCategoryAdmin(newCategoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable long catId) {
        categoryMapping.deleteCategoryAdmin(catId);
    }

    @PostMapping("/compilations")
    public CompilationDto postCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        return compilationMapping.postCompilationAdmin(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable long compId) {
        compilationMapping.deleteCompilationAdmin(compId);
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable long compId, @PathVariable long eventId) {
        compilationMapping.deleteEventFromCompilationAdmin(compId, eventId);
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable long compId, @PathVariable long eventId) {
        compilationMapping.addEventToCompilationAdmin(compId, eventId);
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public void unpinCompilation(@PathVariable long compId) {
        compilationMapping.pinCompilationAdmin(compId, false);
    }

    @PatchMapping("/compilations/{compId}/pin")
    public void pinEvent(@PathVariable long compId) {
        compilationMapping.pinCompilationAdmin(compId, true);
    }
}
