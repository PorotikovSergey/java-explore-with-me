package ru.practicum.explore.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.responses.CategoryResponse;
import ru.practicum.explore.responses.CompilationResponse;
import ru.practicum.explore.responses.EventResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class PublicController {

    private final EventResponse eventResponse;
    private final CategoryResponse categoryResponse;
    private final CompilationResponse compilationResponse;


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

        return new ResponseEntity<>(eventResponse.getEventsPublic(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size, request), HttpStatus.OK);
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Object> getEventById(HttpServletRequest request, @PathVariable long id) {

        return new ResponseEntity<>(eventResponse.getEventByIdPublic(request, id), HttpStatus.OK);
    }

    @GetMapping("/compilations")
    public ResponseEntity<Object> getCompilations(@RequestParam(name = "from", defaultValue = "0")
                                                  Integer from,
                                                  @RequestParam(name = "size", defaultValue = "10")
                                                  Integer size,
                                                  @RequestParam(name = "pinned", defaultValue = "false")
                                                  Boolean pinned) {

        return new ResponseEntity<>(compilationResponse.getCompilationsPublic(pinned, from, size), HttpStatus.OK);

    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<Object> getCompilationById(@PathVariable long compId) {

        return new ResponseEntity<>(compilationResponse.getCompilationByIdPublic(compId), HttpStatus.OK);

    }

    @GetMapping("/categories")
    public ResponseEntity<Object> getCategories(@RequestParam(name = "from", defaultValue = "0")
                                                Integer from,
                                                @RequestParam(name = "size", defaultValue = "10")
                                                Integer size) {

        return new ResponseEntity<>(categoryResponse.getCategoriesPublic(from, size), HttpStatus.OK);

    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<Object> getCategoryById(@PathVariable long catId) {

        return new ResponseEntity<>(categoryResponse.getCategoryByIdPublic(catId), HttpStatus.OK);

    }
}
