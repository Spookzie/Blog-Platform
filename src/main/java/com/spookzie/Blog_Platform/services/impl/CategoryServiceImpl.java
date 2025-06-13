package com.spookzie.Blog_Platform.services.impl;

import com.spookzie.Blog_Platform.domain.entities.Category;
import com.spookzie.Blog_Platform.repositories.CategoryRepository;
import com.spookzie.Blog_Platform.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService
{
    private final CategoryRepository categoryRepo;


    @Override
    public List<Category> listCategories()
    {
        return this.categoryRepo.findAllWithPostCount();
    }
}