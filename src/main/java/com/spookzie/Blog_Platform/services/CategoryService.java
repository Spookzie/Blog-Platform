package com.spookzie.Blog_Platform.services;

import com.spookzie.Blog_Platform.domain.entities.Category;

import java.util.List;

public interface CategoryService
{
    List<Category> listCategories();
}