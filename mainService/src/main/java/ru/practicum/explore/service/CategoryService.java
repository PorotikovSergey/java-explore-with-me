package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.model.Category;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.storage.CategoryRepository;
import ru.practicum.explore.storage.EventRepository;

import javax.xml.bind.ValidationException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private static final String CATEGORY_NOT_FOUND = "Категории по данному id нет в базе";
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public List<Category> getCategoriesPublic(Integer from, Integer size) {
        List<Category> list = categoryRepository.findAll();
        return getPageableList(list, from, size);
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

    public Category deleteCategoryAdmin(long catId) throws ValidationException {
        List<Event> list = eventRepository.findAllByCategoryId(catId);
        if (!list.isEmpty()) {
            throw new ValidationException("Нельзя удалять категорию если есть связанные с ней события");
        }
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND));
        categoryRepository.deleteById(catId);
        return category;
    }

    private List<Category> getPageableList(List<Category> list, int from, int size) {
        PagedListHolder<Category> page = new PagedListHolder<>(list.subList(from, list.size()));
        page.setPageSize(size);
        page.setPage(0);
        return page.getPageList();
    }
}
