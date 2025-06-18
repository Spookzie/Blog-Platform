package com.spookzie.Blog_Platform.controllers;

import com.spookzie.Blog_Platform.domain.dtos.CategoryResponse;
import com.spookzie.Blog_Platform.domain.dtos.CreateCategoryRequest;
import com.spookzie.Blog_Platform.mappers.CategoryMapper;
import com.spookzie.Blog_Platform.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController
{
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;


    @GetMapping
    public ResponseEntity<List<CategoryResponse>> listCategories()
    {
        List<CategoryResponse> categoryResponses = this.categoryService.listCategories()
                .stream()
                .map(this.categoryMapper::toCategoryResponse)
                .toList();


        return new ResponseEntity<>(categoryResponses, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest create_category_request)
    {
        CategoryResponse createdCategoryResponse = this.categoryMapper.toCategoryResponse(
                this.categoryService.createCategory(
                        this.categoryMapper.toEntity(create_category_request)
                )
        );


        return new ResponseEntity<>(createdCategoryResponse, HttpStatus.CREATED);
    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id)
    {
        this.categoryService.deleteCategory(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}