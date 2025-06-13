package com.spookzie.Blog_Platform.repositories;

import com.spookzie.Blog_Platform.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID>
{
    /*  Fetching all the categories and along with them all the posts that are associated with each query   */
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.posts")
    List<Category> findAllWithPostCount();


    boolean existsByNameIgnoreCase(String name);
}