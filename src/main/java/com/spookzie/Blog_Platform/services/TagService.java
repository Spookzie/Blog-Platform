package com.spookzie.Blog_Platform.services;

import com.spookzie.Blog_Platform.domain.entities.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;


public interface TagService
{
    List<Tag> listTags();

    List<Tag> createTags(Set<String> tag_names);

    void deleteTag(UUID id);
}