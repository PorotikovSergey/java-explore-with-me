package ru.practicum.explore.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.CategoryDto;
import ru.practicum.explore.dto.CompilationDto;
import ru.practicum.explore.dto.EventFullDto;
import ru.practicum.explore.dto.EventShortDto;
import ru.practicum.explore.mapping.CategoryMapping;
import ru.practicum.explore.mapping.CompilationMapping;
import ru.practicum.explore.mapping.EventMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@ResponseBody
public class PublicController {

    private final EventMapping eventMapping;
    private final CategoryMapping categoryMapping;
    private final CompilationMapping compilationMapping;


    @GetMapping("/events")
    public List<EventShortDto> getEvents(HttpServletRequest request,
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
        log.info("==ЭНДПОИНТ GET /events");
        log.info("Публичный поиск по всем событиям по параметрам categories = {}, paid = {}, onlyAvailable = {}," +
                        "text = {}, sort = {}, start = {}, end = {}, from = {}, size = {}", categories, paid, onlyAvailable,
                text.substring(0, 20), sort, rangeStart, rangeEnd, from, size);
        return eventMapping.getEventsPublic(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(HttpServletRequest request, @PathVariable long id) {
        log.info("==ЭНДПОИНТ GET /events/{id}");
        log.info("Публичный поиск события с id {}", id);
        return eventMapping.getEventByIdPublic(request, id);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(name = "from", defaultValue = "0")
                                                Integer from,
                                                @RequestParam(name = "size", defaultValue = "10")
                                                Integer size,
                                                @RequestParam(name = "pinned", defaultValue = "false")
                                                Boolean pinned) {
        log.info("==ЭНДПОИНТ GET /compilations");
        log.info("Публичное получение подборок по параметрам pinned = {}, from = {}, size = {}", pinned, from, size);
        return compilationMapping.getCompilationsPublic(pinned, from, size);

    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable long compId) {
        log.info("==ЭНДПОИНТ GET /compilations/{compId}");
        log.info("Публичное получение подборки по id {}", compId);
        return compilationMapping.getCompilationByIdPublic(compId);

    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(name = "from", defaultValue = "0")
                                           Integer from,
                                           @RequestParam(name = "size", defaultValue = "10")
                                           Integer size) {
        log.info("==ЭНДПОИНТ GET /categories");
        log.info("Публичное получение категорий с пагинацией: from = {}, size = {}", from, size);
        return categoryMapping.getCategoriesPublic(from, size);

    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable long catId) {
        log.info("==ЭНДПОИНТ GET /categories/{catId}");
        log.info("Публичное получение категории по id {}", catId);
        return categoryMapping.getCategoryByIdPublic(catId);

    }
}
