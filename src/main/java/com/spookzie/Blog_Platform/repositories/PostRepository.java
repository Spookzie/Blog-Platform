package com.spookzie.Blog_Platform.repositories;

import com.spookzie.Blog_Platform.domain.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface PostRepository extends JpaRepository<Post, UUID>
{
}