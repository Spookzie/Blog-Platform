package com.spookzie.Blog_Platform.services.impl;

import com.spookzie.Blog_Platform.domain.entities.Category;
import com.spookzie.Blog_Platform.repositories.CategoryRepository;
import com.spookzie.Blog_Platform.services.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


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


    @Override
    @Transactional
    public Category createCategory(Category category)
    {
        if(this.categoryRepo.existsByNameIgnoreCase(category.getName()))
            throw new IllegalArgumentException("The category already exists");


        return this.categoryRepo.save(category);
    }


    @Override
    @Transactional
    public void deleteCategory(UUID id)
    {
        Optional<Category> category = this.categoryRepo.findById(id);

        if(category.isPresent())
        {
            if (!category.get().getPosts().isEmpty())
                throw new IllegalStateException("This category has post(s) associated with it");

            this.categoryRepo.deleteById(id);
        }
    }
}