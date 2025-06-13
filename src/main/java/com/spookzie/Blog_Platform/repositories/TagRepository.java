package com.spookzie.Blog_Platform.repositories;

import com.spookzie.Blog_Platform.domain.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface TagRepository extends JpaRepository<Tag, UUID>
{
}