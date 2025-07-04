package com.spookzie.Blog_Platform.controllers;

import com.spookzie.Blog_Platform.domain.dtos.CreatePostRequestDto;
import com.spookzie.Blog_Platform.domain.dtos.PostDto;
import com.spookzie.Blog_Platform.domain.dtos.UpdatePostRequestDto;
import com.spookzie.Blog_Platform.domain.entities.User;
import com.spookzie.Blog_Platform.mappers.PostMapper;
import com.spookzie.Blog_Platform.services.PostService;
import com.spookzie.Blog_Platform.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController
{
    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<PostDto>> listPosts(
            @RequestParam(required = false) UUID category_id,
            @RequestParam(required = false) UUID tag_id
    ) {
        List<PostDto> postDtos = this.postService.getAllPosts(category_id, tag_id)
                .stream()
                .map(this.postMapper::toDto)
                .toList();


        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }


    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> listDrafts(@RequestAttribute UUID user_id)
    {
        User loggedInUser = this.userService.getUserById(user_id);

        List<PostDto> draftDtos = this.postService.getDraftPosts(loggedInUser)
                .stream()
                .map(this.postMapper::toDto)
                .toList();


        return ResponseEntity.ok(draftDtos);
    }


    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody CreatePostRequestDto create_post_request_dto,
            @RequestAttribute UUID user_id
    ){
        User loggedInUser = this.userService.getUserById(user_id);

        PostDto createdPost = this.postMapper.toDto(
                this.postService.createPost(
                        loggedInUser,
                        this.postMapper.toCreatePostRequest(create_post_request_dto)
                )
        );


        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }


    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePostRequestDto update_post_request_dto
    ){
        PostDto updatedPost = this.postMapper.toDto(
                this.postService.updatePost(
                        id,
                        this.postMapper.toUpdatePostRequest(update_post_request_dto)
                )
        );


        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID id)
    {
        PostDto foundPost = this.postMapper.toDto(
                this.postService.getPost(id)
        );


        return ResponseEntity.ok(foundPost);
    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id)
    {
        this.postService.deletePost(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}