package ru.practicum.explore.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.*;
import ru.practicum.explore.dto.AdminUpdateEventRequest;
import ru.practicum.explore.dto.NewUserRequest;
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
        return new ResponseEntity<>(userResponse.getUsersAdmin(ids, from, size), HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<Object> addUser(@RequestBody NewUserRequest newUserRequest) {
        return new ResponseEntity<>(userResponse.addUserAdmin(newUserRequest), HttpStatus.OK);
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
        return new ResponseEntity<>(eventResponse.getEventsAdmin(users, states, categories,
                rangeStart, rangeEnd, from, size), HttpStatus.OK);
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<Object> putEvent(@PathVariable long eventId, @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        return new ResponseEntity<>(eventResponse.putEventAdmin(eventId, adminUpdateEventRequest), HttpStatus.OK);
    }

    @PatchMapping("/events/{eventId}/publish")
    public ResponseEntity<Object> publishEvent(@PathVariable long eventId) {
        return new ResponseEntity<>(eventResponse.publishEventAdmin(eventId), HttpStatus.OK);
    }

    @PatchMapping("/events/{eventId}/reject")
    public ResponseEntity<Object> rejectEvent(@PathVariable long eventId) {
        return new ResponseEntity<>(eventResponse.rejectEventAdmin(eventId), HttpStatus.OK);
    }

    @PatchMapping("/categories")
    public ResponseEntity<Object> patchCategory(@RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryResponse.patchCategoryAdmin(categoryDto), HttpStatus.OK);
    }

    @PostMapping("/categories")
    public ResponseEntity<Object> postCategory(@RequestBody NewCategoryDto newCategoryDto) {
        return new ResponseEntity<>(categoryResponse.postCategoryAdmin(newCategoryDto), HttpStatus.OK);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable long catId) {
        categoryResponse.deleteCategoryAdmin(catId);
    }

    @PostMapping("/compilations")
    public ResponseEntity<Object> postCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        return new ResponseEntity<>(compilationResponse.postCompilationAdmin(newCompilationDto), HttpStatus.OK);
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable long compId) {
        compilationResponse.deleteCompilationAdmin(compId);
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable long compId, @PathVariable long eventId) {
        compilationResponse.deleteEventFromCompilationAdmin(compId, eventId);
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable long compId, @PathVariable long eventId) {
        compilationResponse.addEventToCompilationAdmin(compId, eventId);
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public void unpinCompilation(@PathVariable long compId) {
        compilationResponse.unpinCompilationAdmin(compId);
    }

    @PatchMapping("/compilations/{compId}/pin")
    public void pinEvent(@PathVariable long compId) {
        compilationResponse.pinCompilationAdmin(compId);
    }
}
