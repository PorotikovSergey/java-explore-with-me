package ru.practicum.explore.responses;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.Mapper;
import ru.practicum.explore.dto.CategoryDto;
import ru.practicum.explore.dto.NewCategoryDto;
import ru.practicum.explore.model.ApiError;
import ru.practicum.explore.model.Category;
import ru.practicum.explore.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryResponse {

    private final CategoryService categoryService;
    private final Mapper mapper;

    @Autowired
    public CategoryResponse(CategoryService categoryService, Mapper mapper) {
        this.categoryService = categoryService;
        this.mapper = mapper;
    }

    public ResponseEntity<Object> patchCategoryAdmin(CategoryDto categoryDto) {
        Category category = mapper.fromDtoToCategory(categoryDto);
        Category backCategory = categoryService.patchCategoryAdmin(category);
        if(backCategory==null) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        CategoryDto resultCategoryDto = mapper.fromCategoryToDto(backCategory);
        return new ResponseEntity<>(resultCategoryDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> postCategoryAdmin(NewCategoryDto newCategoryDto) {
        Category category = mapper.fromNewDtoToCategory(newCategoryDto);
        Category backCategory = categoryService.postCategoryAdmin(category);
        if(backCategory==null) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        CategoryDto resultCategoryDto = mapper.fromCategoryToDto(backCategory);
        return new ResponseEntity<>(resultCategoryDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteCategoryAdmin(long catId) {
        Category backCategory = categoryService.deleteCategoryAdmin(catId);
        if(backCategory==null) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        CategoryDto resultCategoryDto = mapper.fromCategoryToDto(backCategory);
        return new ResponseEntity<>(resultCategoryDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> getCategoriesPublic(Integer from, Integer size) {
        List<Category> list = categoryService.getCategoriesPublic(from, size);
        if(list.isEmpty()) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        List<CategoryDto> resultList = list.stream().map(mapper::fromCategoryToDto).collect(Collectors.toList());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> getCategoryByIdPublic(long catId) {
        Category category = categoryService.getCategoryByIdPublic(catId);
        if(category == null) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        CategoryDto resultCategoryDto = mapper.fromCategoryToDto(category);
        return new ResponseEntity<>(resultCategoryDto, HttpStatus.OK);
    }
}
