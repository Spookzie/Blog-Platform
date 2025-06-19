package com.spookzie.Blog_Platform.services;

import com.spookzie.Blog_Platform.domain.CreatePostRequest;
import com.spookzie.Blog_Platform.domain.UpdatePostRequest;
import com.spookzie.Blog_Platform.domain.entities.Post;
import com.spookzie.Blog_Platform.domain.entities.User;

import java.util.List;
import java.util.UUID;


public interface PostService
{
    Post getPost(UUID id);

    List<Post> getAllPosts(UUID category_id, UUID tag_id);

    List<Post> getDraftPosts(User user);

    Post createPost(User user, CreatePostRequest create_post_request);

    Post updatePost(UUID id, UpdatePostRequest update_post_request);

    void deletePost(UUID id);
}