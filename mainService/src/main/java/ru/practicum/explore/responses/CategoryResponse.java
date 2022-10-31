package ru.practicum.explore.responses;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.CategoryDto;
import ru.practicum.explore.dto.NewCategoryDto;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.model.Category;
import ru.practicum.explore.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryResponse {
    private final CategoryService categoryService;
    private final Mapper mapper;

    public CategoryDto patchCategoryAdmin(CategoryDto categoryDto) {
        Category category = mapper.fromDtoToCategory(categoryDto);

        Category backCategory = categoryService.patchCategoryAdmin(category);

        return mapper.fromCategoryToDto(backCategory);
    }

    public CategoryDto postCategoryAdmin(NewCategoryDto newCategoryDto) {
        Category category = mapper.fromNewDtoToCategory(newCategoryDto);

        Category backCategory = categoryService.postCategoryAdmin(category);

        return mapper.fromCategoryToDto(backCategory);
    }

    public void deleteCategoryAdmin(long catId) {
        categoryService.deleteCategoryAdmin(catId);
    }

    public List<CategoryDto> getCategoriesPublic(Integer from, Integer size) {
        List<Category> list = categoryService.getCategoriesPublic(from, size);

        if (list.isEmpty()) {
            throw new NotFoundException("Категории не найдены");
        }

        return list.stream().map(mapper::fromCategoryToDto).collect(Collectors.toList());
    }

    public CategoryDto getCategoryByIdPublic(long catId) {

        Category category = categoryService.getCategoryByIdPublic(catId);

        return mapper.fromCategoryToDto(category);
    }
}
