package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.exceptions.ValidationException;
import ru.practicum.explore.model.Category;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.storage.CategoryRepository;
import ru.practicum.explore.storage.EventRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private static final String CATEGORY_NOT_FOUND = "Категории по данному id нет в базе";
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public List<Category> getCategoriesPublic(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Category> result = categoryRepository.findAll(pageable).getContent();
        log.info("Из бд получен следующий лист категорий: {}", result);
        return result;
    }

    public Category getCategoryByIdPublic(long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND));
        log.info("Из бд получена следующая категория: {}", category);
        return category;
    }

    public Category patchCategoryAdmin(Category category) {
        Category categoryForPatch = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND));
        categoryForPatch.setName(category.getName());
        categoryRepository.save(categoryForPatch);
        log.info("После изменения категория такая : {}", categoryForPatch);
        return categoryForPatch;
    }

    public Category postCategoryAdmin(Category category) {
        categoryRepository.save(category);
        log.info("В бд добавлена категория : {}", category);
        return category;
    }

    public void deleteCategoryAdmin(long catId) {
        List<Event> list = eventRepository.findAllByCategoryId(catId);
        if (!list.isEmpty()) {
            throw new ValidationException("Нельзя удалять категорию если есть связанные с ней события");
        }
        categoryRepository.deleteById(catId);
        log.info("Из бд удалена категория с id: {}", catId);
    }
}
