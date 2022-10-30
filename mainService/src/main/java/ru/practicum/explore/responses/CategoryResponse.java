package ru.practicum.explore.responses;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.Mapper;
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

    public ResponseEntity<Object> patchCategoryAdmin(CategoryDto categoryDto) {
        Category category = mapper.fromDtoToCategory(categoryDto);

        Category backCategory = categoryService.patchCategoryAdmin(category);

        CategoryDto resultCategoryDto = mapper.fromCategoryToDto(backCategory);
        return new ResponseEntity<>(resultCategoryDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> postCategoryAdmin(NewCategoryDto newCategoryDto) {
        Category category = mapper.fromNewDtoToCategory(newCategoryDto);

        Category backCategory = categoryService.postCategoryAdmin(category);

        CategoryDto resultCategoryDto = mapper.fromCategoryToDto(backCategory);
        return new ResponseEntity<>(resultCategoryDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteCategoryAdmin(long catId) {

        Category backCategory = categoryService.deleteCategoryAdmin(catId);

        CategoryDto resultCategoryDto = mapper.fromCategoryToDto(backCategory);
        return new ResponseEntity<>(resultCategoryDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> getCategoriesPublic(Integer from, Integer size) {
        List<Category> list = categoryService.getCategoriesPublic(from, size);

        if (list.isEmpty()) {
            throw new NotFoundException("Категории не найдены");
        }

        List<CategoryDto> resultList = list.stream().map(mapper::fromCategoryToDto).collect(Collectors.toList());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> getCategoryByIdPublic(long catId) {

        Category category = categoryService.getCategoryByIdPublic(catId);

        CategoryDto resultCategoryDto = mapper.fromCategoryToDto(category);
        return new ResponseEntity<>(resultCategoryDto, HttpStatus.OK);
    }
}
