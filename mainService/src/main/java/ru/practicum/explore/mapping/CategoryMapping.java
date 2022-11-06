package ru.practicum.explore.mapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class CategoryMapping {
    private final CategoryService categoryService;

    public CategoryDto patchCategoryAdmin(CategoryDto categoryDto) {
        Category category = fromDtoToCategory(categoryDto);

        Category backCategory = categoryService.patchCategoryAdmin(category);

        return fromCategoryToDto(backCategory);
    }

    public CategoryDto postCategoryAdmin(NewCategoryDto newCategoryDto) {
        Category category = fromNewDtoToCategory(newCategoryDto);

        Category backCategory = categoryService.postCategoryAdmin(category);

        return fromCategoryToDto(backCategory);
    }

    public void deleteCategoryAdmin(long catId) {
        categoryService.deleteCategoryAdmin(catId);
    }

    public List<CategoryDto> getCategoriesPublic(Integer from, Integer size) {
        List<Category> list = categoryService.getCategoriesPublic(from, size);

        if (list.isEmpty()) {
            throw new NotFoundException("Категории не найдены");
        }

        return list.stream().map(CategoryMapping::fromCategoryToDto).collect(Collectors.toList());
    }

    public CategoryDto getCategoryByIdPublic(long catId) {

        Category category = categoryService.getCategoryByIdPublic(catId);

        return fromCategoryToDto(category);
    }

    public static CategoryDto fromCategoryToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public static Category fromDtoToCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }

    public static Category fromNewDtoToCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }
}
