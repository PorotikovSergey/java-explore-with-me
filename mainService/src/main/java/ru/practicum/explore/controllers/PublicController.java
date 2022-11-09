package ru.practicum.explore.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.*;
import ru.practicum.explore.mapping.CategoryMapping;
import ru.practicum.explore.mapping.CompilationMapping;
import ru.practicum.explore.mapping.EventMapping;
import ru.practicum.explore.mapping.ReviewMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class PublicController {

    private final EventMapping eventMapping;
    private final CategoryMapping categoryMapping;
    private final CompilationMapping compilationMapping;
    private final ReviewMapping reviewMapping;

    //--------------------------------ФИЧА------------------------------------------

    //в публичном контроллере неавторизированные пользователи могут только посмотреть комменты к событию
    //добавление, удаление, оценка - это всё в приватном контроллере
    @GetMapping("/events/{eventId}/reviews")
    public List<ReviewDto> getAllReviewsForEvent(@PathVariable long eventId,
                                                 @RequestParam(name = "from", defaultValue = "0")
                                                 Integer from,
                                                 @RequestParam(name = "size", defaultValue = "10")
                                                 Integer size,
                                                 @RequestParam(name = "sort", defaultValue = "REVIEW_RATING")
                                                 String sort) {

        log.info("==ЭНДПОИНТ GET /events/{eventId}/reviews");
        log.info("Публичный поиск отзывов на событие {}", eventId);
        return reviewMapping.getReviews(eventId, from, size, sort);
    }
    //------------------------------------------------------------------------------


    @GetMapping("/events")
    public List<EventShortDto> getEvents(HttpServletRequest request,
                                         @RequestParam(name = "from", defaultValue = "0", required = false)
                                         Integer from,
                                         @RequestParam(name = "size", defaultValue = "10", required = false)
                                         Integer size,
                                         @RequestParam(name = "text", required = false)
                                         String text,
                                         @RequestParam(name = "categories", required = false)
                                         List<Long> categories,
                                         @RequestParam(name = "paid", required = false)
                                         Boolean paid,
                                         @RequestParam(name = "rangeStart", defaultValue = "2021-01-06 12:12:12", required = false)
                                         String rangeStart,
                                         @RequestParam(name = "rangeEnd", defaultValue = "2097-01-06 12:12:12", required = false)
                                         String rangeEnd,
                                         @RequestParam(name = "onlyAvailable", required = false)
                                         Boolean onlyAvailable,
                                         @RequestParam(name = "sort", defaultValue = "EVENT_DATE", required = false)
                                         String sort) {
        log.info("==ЭНДПОИНТ GET /events");
        log.info("Публичный поиск по всем событиям по параметрам categories = {}, paid = {}, onlyAvailable = {}," +
                        "text = {}, sort = {}, start = {}, end = {}, from = {}, size = {}", categories, paid,
                onlyAvailable, text.substring(0, 20), sort, rangeStart, rangeEnd, from, size);
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
