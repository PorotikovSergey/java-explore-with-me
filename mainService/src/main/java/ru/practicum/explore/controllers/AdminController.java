package ru.practicum.explore.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.*;
import ru.practicum.explore.model.AdminUpdateEventRequest;
import ru.practicum.explore.model.NewUserRequest;
import ru.practicum.explore.responses.CategoryResponse;
import ru.practicum.explore.responses.CompilationResponse;
import ru.practicum.explore.responses.EventResponse;
import ru.practicum.explore.responses.UserResponse;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserResponse userResponse;
    private final EventResponse eventResponse;
    private final CategoryResponse categoryResponse;
    private final CompilationResponse compilationResponse;

    public AdminController(UserResponse userResponse, EventResponse eventResponse,
                           CategoryResponse categoryResponse, CompilationResponse compilationResponse) {
        this.userResponse = userResponse;
        this.eventResponse = eventResponse;
        this.categoryResponse = categoryResponse;
        this.compilationResponse = compilationResponse;
    }


    @GetMapping("/users")
    public ResponseEntity<Object> getUsers(@RequestParam(name = "ids")
                                           List<Long> ids,
                                           @RequestParam(name = "from", defaultValue = "0")
                                           Integer from,
                                           @RequestParam(name = "size", defaultValue = "10")
                                           Integer size) {
        return userResponse.getUsersAdmin(ids, from, size);
    }

    @PostMapping("/users")
    public ResponseEntity<Object> addUser(@RequestBody NewUserRequest newUserRequest) {
        return userResponse.addUserAdmin(newUserRequest);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userResponse.deleteUserAdmin(userId);
    }

    @GetMapping("/events")
    public ResponseEntity<Object> getEvents(@RequestParam(name = "users")
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
        return eventResponse.getEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<Object> putEvent(@PathVariable long eventId, @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        return eventResponse.putEventAdmin(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/events/{eventId}/publish")
    public ResponseEntity<Object> publishEvent(@PathVariable long eventId) {
        return eventResponse.publishEventAdmin(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    public ResponseEntity<Object> rejectEvent(@PathVariable long eventId) {
        return eventResponse.rejectEventAdmin(eventId);
    }

    @PatchMapping("/categories")
    public ResponseEntity<Object> patchCategory(@RequestBody CategoryDto categoryDto) {
        return categoryResponse.patchCategoryAdmin(categoryDto);
    }

    @PostMapping("/categories")
    public ResponseEntity<Object> postCategory(@RequestBody NewCategoryDto newCategoryDto) {
        return categoryResponse.postCategoryAdmin(newCategoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable long catId) {
        return categoryResponse.deleteCategoryAdmin(catId);
    }

    @PostMapping("/compilations")
    public ResponseEntity<Object> postCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        return compilationResponse.postCompilationAdmin(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    public ResponseEntity<Object> deleteCompilation(@PathVariable long compId) {
        return compilationResponse.deleteCompilationAdmin(compId);
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public ResponseEntity<Object> deleteEventFromCompilation(@PathVariable long compId, @PathVariable long eventId) {
        return compilationResponse.deleteEventFromCompilationAdmin(compId, eventId);
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public ResponseEntity<Object> addEventToCompilation(@PathVariable long compId, @PathVariable long eventId) {
        return compilationResponse.addEventToCompilationAdmin(compId, eventId);
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public ResponseEntity<Object> unpinCompilation(@PathVariable long compId) {
        return compilationResponse.unpinCompilationAdmin(compId);
    }

    @PatchMapping("/compilations/{compId}/pin")
    public ResponseEntity<Object> pinEvent(@PathVariable long compId) {
        return compilationResponse.pinCompilationAdmin(compId);
    }
}
