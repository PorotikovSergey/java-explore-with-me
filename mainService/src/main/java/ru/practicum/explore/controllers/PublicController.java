package ru.practicum.explore.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.apierrors.ServerApiError;
import ru.practicum.explore.auxiliary.EndpointHit;
import ru.practicum.explore.auxiliary.FromMainToStatsClient;
import ru.practicum.explore.responses.CategoryResponse;
import ru.practicum.explore.responses.CompilationResponse;
import ru.practicum.explore.responses.EventResponse;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class PublicController {

    private final EventResponse eventResponse;
    private final CategoryResponse categoryResponse;
    private final CompilationResponse compilationResponse;
    private final FromMainToStatsClient fromMainToStatsClient;

    @Autowired
    public PublicController(EventResponse eventResponse, CategoryResponse categoryResponse,
                            CompilationResponse compilationResponse, FromMainToStatsClient fromMainToStatsClient) {
        this.eventResponse = eventResponse;
        this.categoryResponse = categoryResponse;
        this.compilationResponse = compilationResponse;
        this.fromMainToStatsClient = fromMainToStatsClient;
    }

    @GetMapping("/events")
    public ResponseEntity<Object> getEvents(HttpServletRequest request,
                                            @RequestParam(name = "from", defaultValue = "0")
                                            Integer from,
                                            @RequestParam(name = "size", defaultValue = "10")
                                            Integer size,
                                            @RequestParam(name = "text")
                                            String text,
                                            @RequestParam(name = "categories")
                                            List<Long> categories,
                                            @RequestParam(name = "paid", defaultValue = "false")
                                            Boolean paid,
                                            @RequestParam(name = "rangeStart")
                                            String rangeStart,
                                            @RequestParam(name = "rangeEnd")
                                            String rangeEnd,
                                            @RequestParam(name = "onlyAvailable", defaultValue = "false")
                                            Boolean onlyAvailable,
                                            @RequestParam(name = "sort", defaultValue = "EVENT_DATE")
                                            String sort) {

        try {
            EndpointHit hit = new EndpointHit(0, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().toString());
            fromMainToStatsClient.postHit(hit);
        } catch (Exception e) {
            log.error("Почему-то запрос в статистику не сработал");
        }

        try {
            return eventResponse.getEventsPublic(text, categories, paid, rangeStart,
                    rangeEnd, onlyAvailable, sort, from, size);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerApiError.getServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Object> getEventById(HttpServletRequest request, @PathVariable long id) {
        try {
            EndpointHit hit = new EndpointHit(0, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().toString());
            fromMainToStatsClient.postHit(hit);
        } catch (Exception e) {
            log.error("Почему-то запрос в статистику не сработал");
        }
        try {
            return eventResponse.getEventByIdPublic(id);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerApiError.getServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/compilations")
    public ResponseEntity<Object> getCompilations(@RequestParam(name = "from", defaultValue = "0")
                                                  Integer from,
                                                  @RequestParam(name = "size", defaultValue = "10")
                                                  Integer size,
                                                  @RequestParam(name = "pinned", defaultValue = "false")
                                                  Boolean pinned) {
        try {
            return compilationResponse.getCompilationsPublic(pinned, from, size);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerApiError.getServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<Object> getCompilationById(@PathVariable long compId) {
        try {
            return compilationResponse.getCompilationByIdPublic(compId);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerApiError.getServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<Object> getCategories(@RequestParam(name = "from", defaultValue = "0")
                                                Integer from,
                                                @RequestParam(name = "size", defaultValue = "10")
                                                Integer size) {
        try {
            return categoryResponse.getCategoriesPublic(from, size);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerApiError.getServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<Object> getCategoryById(@PathVariable long catId) {
        try {
            return categoryResponse.getCategoryByIdPublic(catId);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerApiError.getServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
