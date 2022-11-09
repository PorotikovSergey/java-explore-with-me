package ru.practicum.explore.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.explore.dto.*;
import ru.practicum.explore.dto.AdminUpdateEventRequest;
import ru.practicum.explore.dto.NewUserRequest;
import ru.practicum.explore.mapping.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserMapping userMapping;
    private final EventMapping eventMapping;
    private final CategoryMapping categoryMapping;
    private final CompilationMapping compilationMapping;
    private final ReviewMapping reviewMapping;

    //--------------------------------ФИЧА------------------------------------------

    @PatchMapping("/reviews/{reviewId}")
    public void patchReview(@PathVariable long reviewId,
                             @RequestParam(name = "decision", defaultValue = "false") Boolean decision) {
        log.info("==ЭНДПОИНТ PATCH admin/reviews/{reviewId}?decision=");
        log.info("Патч админом отзыва с id: {}", reviewId);

        reviewMapping.patchReviewByAdmin(reviewId, decision);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public void patchReview(@PathVariable long reviewId) {
        log.info("==ЭНДПОИНТ DELETE admin/reviews/{reviewId}");
        log.info("Удаление админом отзыва с id: {}", reviewId);

        reviewMapping.deleteReviewByAdmin(reviewId);
    }
    //------------------------------------------------------------------------------

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(name = "ids")
                                  List<Long> ids,
                                  @RequestParam(name = "from", defaultValue = "0")
                                  Integer from,
                                  @RequestParam(name = "size", defaultValue = "10")
                                  Integer size) {
        log.info("==ЭНДПОИНТ GET admin/users");
        log.info("Поиск пользователей по ids: {} c {} по {} штук", ids, from, size);
        return userMapping.getUsersAdmin(ids, from, size);
    }

    @PostMapping("/users")
    public UserDto addUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("==ЭНДПОИНТ POST admin/users");
        log.info("Админ добавляет пользователя: {}", newUserRequest);
        return userMapping.addUserAdmin(newUserRequest);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("==ЭНДПОИНТ DELETE admin/users/{userId}");
        log.info("Админ удаляет пользователя с id: {}", userId);
        userMapping.deleteUserAdmin(userId);
    }

    @GetMapping("/events")
    public List<EventFullDto> getEvents(@RequestParam(name = "users", required = false)
                                        List<Long> users,
                                        @RequestParam(name = "states", required = false)
                                        List<String> states,
                                        @RequestParam(name = "categories", required = false)
                                        List<Long> categories,
                                        @RequestParam(name = "rangeStart", defaultValue = "2022-01-06 12:12:12")
                                        String rangeStart,
                                        @RequestParam(name = "rangeEnd", defaultValue = "2097-01-06 12:12:12")
                                        String rangeEnd,
                                        @RequestParam(name = "from", defaultValue = "0")
                                        Integer from,
                                        @RequestParam(name = "size", defaultValue = "10")
                                        Integer size) {

        log.info("==ЭНДПОИНТ GET admin/events");
        log.info("Админ получает события по параметрам: users = {}, states = {}, categories = {}, между {} и {} , " +
                "и пагинацией от {} по {}", users, states, categories, rangeStart, rangeEnd, from, size);
        return eventMapping.getEventsAdmin(users, states, categories,
                rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/events/{eventId}")
    public EventFullDto putEvent(@PathVariable long eventId,
                                 @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("==ЭНДПОИНТ PUT admin/events/{eventId}");
        log.info("Админ изменяет событие с id {} на новое {}", eventId, adminUpdateEventRequest);
        return eventMapping.putEventAdmin(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable long eventId) {
        log.info("==ЭНДПОИНТ PATCH admin/events/{eventId}/publish");
        log.info("Админ публикует событие с id {}", eventId);
        return eventMapping.publishEventAdmin(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable long eventId) {
        log.info("==ЭНДПОИНТ PATCH admin/events/{eventId}/reject");
        log.info("Админ отменяет событие с id {}", eventId);
        return eventMapping.rejectEventAdmin(eventId);
    }

    @PatchMapping("/categories")
    public CategoryDto patchCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("==ЭНДПОИНТ PATCH admin/categories");
        log.info("Админ изменяет категорию {}", categoryDto);
        return categoryMapping.patchCategoryAdmin(categoryDto);
    }

    @PostMapping("/categories")
    public CategoryDto postCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("==ЭНДПОИНТ POST admin/categories");
        log.info("Админ постит категорию {}", newCategoryDto);
        return categoryMapping.postCategoryAdmin(newCategoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable long catId) {
        log.info("==ЭНДПОИНТ DELETE admin/categories/{catId}");
        log.info("Админ удаляет категорию {}", catId);
        categoryMapping.deleteCategoryAdmin(catId);
    }

    @PostMapping("/compilations")
    public CompilationDto postCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("==ЭНДПОИНТ POST admin/compilations");
        log.info("Админ постит подборку {}", newCompilationDto);
        return compilationMapping.postCompilationAdmin(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable long compId) {
        log.info("==ЭНДПОИНТ DELETE admin/compilations");
        log.info("Админ удаляет подборку с id {}", compId);
        compilationMapping.deleteCompilationAdmin(compId);
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable long compId, @PathVariable long eventId) {
        log.info("==ЭНДПОИНТ DELETE admin/compilations/{compId}/events/{eventId}");
        log.info("Админ удаляет событие с id {} из подборки с id {}", eventId, compId);
        compilationMapping.deleteEventFromCompilationAdmin(compId, eventId);
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable long compId, @PathVariable long eventId) {
        log.info("==ЭНДПОИНТ PATCH admin/compilations/{compId}/events/{eventId}");
        log.info("Админ добавляет событие с id {} в подборку с id {}", eventId, compId);
        compilationMapping.addEventToCompilationAdmin(compId, eventId);
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public void unpinCompilation(@PathVariable long compId) {
        log.info("==ЭНДПОИНТ DELETE admin/compilations/{compId}/pin");
        log.info("Админ открепляет подборку с id {} от главной страницы", compId);
        compilationMapping.pinCompilationAdmin(compId, false);
    }

    @PatchMapping("/compilations/{compId}/pin")
    public void pinEvent(@PathVariable long compId) {
        log.info("==ЭНДПОИНТ PATCH admin/compilations/{compId}/pin");
        log.info("Админ прикрепляет подборку с id {} на главную страницу", compId);
        compilationMapping.pinCompilationAdmin(compId, true);
    }
}
