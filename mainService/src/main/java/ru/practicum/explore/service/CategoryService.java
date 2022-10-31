package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.support.PagedListHolder;
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
        return categoryRepository.findAll(pageable).getContent();
    }

    public Category getCategoryByIdPublic(long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND));
    }

    public Category patchCategoryAdmin(Category category) {
        Category categoryForPatch = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND));
        categoryForPatch.setName(category.getName());
        categoryRepository.save(categoryForPatch);
        return categoryForPatch;
    }

    public Category postCategoryAdmin(Category category) {
        categoryRepository.save(category);
        return category;
    }

    public void deleteCategoryAdmin(long catId) {
        List<Event> list = eventRepository.findAllByCategoryId(catId);
        if (!list.isEmpty()) {
            throw new ValidationException("Нельзя удалять категорию если есть связанные с ней события");
        }

        try {
            categoryRepository.deleteById(catId);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException(CATEGORY_NOT_FOUND);
        }
    }
}
