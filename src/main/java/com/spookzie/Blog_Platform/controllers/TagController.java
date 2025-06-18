package com.spookzie.Blog_Platform.controllers;

import com.spookzie.Blog_Platform.domain.dtos.CreateTagsRequest;
import com.spookzie.Blog_Platform.domain.dtos.TagResponse;
import com.spookzie.Blog_Platform.mappers.TagMapper;
import com.spookzie.Blog_Platform.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController
{
    private final TagService tagService;
    private final TagMapper tagMapper;


    @GetMapping
    public ResponseEntity<List<TagResponse>> listTags()
    {
        List<TagResponse> tagResponses = this.tagService.listTags()
                .stream()
                .map(this.tagMapper::toTagResponse)
                .toList();


        return new ResponseEntity<>(tagResponses, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<List<TagResponse>> createTags(@Valid @RequestBody CreateTagsRequest create_tags_request)
    {
        List<TagResponse> createdTags = this.tagService.createTags(create_tags_request.getNames())
                .stream()
                .map(this.tagMapper::toTagResponse)
                .toList();


        return new ResponseEntity<>(createdTags, HttpStatus.CREATED);
    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id)
    {
        this.tagService.deleteTag(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}