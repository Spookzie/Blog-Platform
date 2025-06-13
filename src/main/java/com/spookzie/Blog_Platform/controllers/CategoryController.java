package com.spookzie.Blog_Platform.controllers;

import com.spookzie.Blog_Platform.domain.dtos.CategoryDto;
import com.spookzie.Blog_Platform.mappers.CategoryMapper;
import com.spookzie.Blog_Platform.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController
{
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;


    @GetMapping
    public ResponseEntity<List<CategoryDto>> listCategories()
    {
        List<CategoryDto> categoryDtos= this.categoryService.listCategories()
                .stream()
                .map(this.categoryMapper::toDto)
                .toList();


        return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
    }
}