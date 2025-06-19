package com.spookzie.Blog_Platform.services.impl;

import com.spookzie.Blog_Platform.domain.entities.Tag;
import com.spookzie.Blog_Platform.repositories.TagRepository;
import com.spookzie.Blog_Platform.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService
{
    private final TagRepository tagRepo;


    @Override
    public List<Tag> listTags()
    {
        return this.tagRepo.findAllWithPostCount();
    }


    @Transactional
    @Override
    public List<Tag> createTags(Set<String> tag_names)
    {
        List<Tag> existingTags = this.tagRepo.findByNameIn(tag_names);
        Set<String> existingTagsNames = existingTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        List<Tag> newTags = tag_names.stream()
                .filter(name -> !existingTagsNames.contains(name))
                .map(name -> Tag.builder()
                        .name(name)
                        .posts(new HashSet<>())
                        .build())
                .toList();

        List<Tag> savedTags = new ArrayList<>();
        if(!newTags.isEmpty())
            savedTags = this.tagRepo.saveAll(newTags);

        savedTags.addAll(existingTags);


        return savedTags;
    }


    @Transactional
    @Override
    public void deleteTag(UUID id)
    {
        this.tagRepo.findById(id).ifPresent(tag -> {
            if(!tag.getPosts().isEmpty())
                throw new IllegalStateException("Cannot delete tag with posts");

            this.tagRepo.deleteById(id);
        });
    }


    @Override
    public Tag getTagById(UUID id)
    {
        return this.tagRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id " + id));
    }


    @Override
    public List<Tag> getTagsByIds(Set<UUID> ids)
    {
        List<Tag> foundTags = this.tagRepo.findAllById(ids);

        if(foundTags.size() != ids.size())
            throw new EntityNotFoundException("Not all specified tag IDs exist");

        return foundTags;
    }
}