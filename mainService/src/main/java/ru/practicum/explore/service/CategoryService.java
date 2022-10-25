package ru.practicum.explore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.Category;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.storage.CategoryRepository;
import ru.practicum.explore.storage.EventRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    public List<Category> getCategoriesPublic(Integer from, Integer size) {
        List<Category> list = categoryRepository.findAll();
        List<Category> afterPageableList = getPageableList(list, from, size);
        return afterPageableList;
    }

    public Category getCategoryByIdPublic(long catId) {
        Optional<Category> optional = categoryRepository.findById(catId);
        if(optional.isPresent()) {
            Category category = optional.get();
            return category;
        } else {
            return null;
        }
    }

    public Category patchCategoryAdmin(Category category) {
        Optional<Category> optional = categoryRepository.findById(category.getId());
        if(optional.isPresent()) {
            Category categoryForPatch = optional.get();
            categoryForPatch.setName(category.getName());
            categoryRepository.save(categoryForPatch);
            return categoryForPatch;
        }
        return null;
    }

    public Category postCategoryAdmin(Category category) {
        categoryRepository.save(category);
        return category;
    }

    public Category deleteCategoryAdmin(long catId) {
        List<Event> list = eventRepository.findAllByCategoryId(catId);
        if(!list.isEmpty()) {
            return null;
        }
        Optional<Category> optional = categoryRepository.findById(catId);
        if(optional.isPresent()) {
            Category category = optional.get();
            categoryRepository.deleteById(catId);
            return category;
        }
        return null;
    }

    private List<Category> getPageableList(List<Category> list, int from, int size) {
        PagedListHolder<Category> page = new PagedListHolder<>(list.subList(from, list.size()));
        page.setPageSize(size);
        page.setPage(0);
        return page.getPageList();
    }
}
