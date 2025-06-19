package com.spookzie.Blog_Platform.services.impl;

import com.spookzie.Blog_Platform.domain.CreatePostRequest;
import com.spookzie.Blog_Platform.domain.PostStatus;
import com.spookzie.Blog_Platform.domain.UpdatePostRequest;
import com.spookzie.Blog_Platform.domain.entities.Category;
import com.spookzie.Blog_Platform.domain.entities.Post;
import com.spookzie.Blog_Platform.domain.entities.Tag;
import com.spookzie.Blog_Platform.domain.entities.User;
import com.spookzie.Blog_Platform.repositories.PostRepository;
import com.spookzie.Blog_Platform.services.CategoryService;
import com.spookzie.Blog_Platform.services.PostService;
import com.spookzie.Blog_Platform.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService
{
    private final PostRepository postRepo;
    private final CategoryService categoryService;
    private final TagService tagService;

    private static final int WORDS_PER_MINUTE = 200;


    @Override
    @Transactional(readOnly = true)
    public Post getPost(UUID id)
    {
        return this.postRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + id));
    }


    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID category_id, UUID tag_id)
    {
        if(category_id != null && tag_id != null)
        {
            Category category = this.categoryService.getCategoryById(category_id);
            Tag tag = this.tagService.getTagById(tag_id);

            return this.postRepo.findAllByStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED,
                    category,
                    tag
            );
        }

        if(category_id != null)     // If tag id is null
        {
            Category category = this.categoryService.getCategoryById(category_id);

            return this.postRepo.findAllByStatusAndCategory(
                    PostStatus.PUBLISHED,
                    category
            );
        }

        if(tag_id != null)      // If category id null
        {
            Tag tag = this.tagService.getTagById(tag_id);

            return this.postRepo.findAllByStatusAndTagsContaining(
                    PostStatus.PUBLISHED,
                    tag
            );
        }

        // If both are null
        return this.postRepo.findAllByStatus(PostStatus.PUBLISHED);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Post> getDraftPosts(User user)
    {
        return this.postRepo.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }


    @Override
    public Post createPost(User user, CreatePostRequest create_post_request)
    {
        Post newPost = new Post();
        newPost.setTitle(create_post_request.getTitle());
        newPost.setContent(create_post_request.getContent());
        newPost.setStatus(create_post_request.getStatus());
        newPost.setAuthor(user);

        newPost.setReadingTime(this.calculateReadingTime(
                create_post_request.getContent()
        ));

        newPost.setCategory(this.categoryService.getCategoryById(
                create_post_request.getCategoryId()
        ));

        Set<UUID> tagIds = create_post_request.getTagIds();
        List<Tag> tags = this.tagService.getTagsByIds(tagIds);
        newPost.setTags(new HashSet<>(tags));

        LocalDateTime now = LocalDateTime.now();
        newPost.setCreatedAt(now);
        newPost.setUpdatedAt(now);


        return this.postRepo.save(newPost);
    }

    private int calculateReadingTime(String content)
    {
        int wordCount = content.trim().split("\\s+").length;

        return (int) Math.ceil(
                (double) wordCount /
                        WORDS_PER_MINUTE
        );
    }


    @Override
    public Post updatePost(UUID id, UpdatePostRequest update_post_request)
    {
        Post existingPost = this.postRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + id));

        existingPost.setTitle(update_post_request.getTitle());
        existingPost.setContent(update_post_request.getContent());
        existingPost.setStatus(update_post_request.getStatus());

        existingPost.setReadingTime(
                this.calculateReadingTime(update_post_request.getContent())
        );

        UUID updatePostRequestCategoryId = update_post_request.getCategoryId();
        UUID existingPostCategoryId = existingPost.getCategory().getId();
        if(!existingPostCategoryId.equals(updatePostRequestCategoryId))
        {
            existingPost.setCategory(
                    this.categoryService.getCategoryById(updatePostRequestCategoryId)
            );
        }

        Set<UUID> updatePostTagIds = update_post_request.getTagIds();
        Set<UUID> existingPostTagIds = existingPost.getTags()
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());
        if(!existingPostTagIds.equals(updatePostTagIds))
        {
            List<Tag> newTags = this.tagService.getTagsByIds(updatePostTagIds);
            existingPost.setTags(new HashSet<>(newTags));
        }


        return this.postRepo.save(existingPost);
    }


    @Override
    public void deletePost(UUID id)
    {
        Post post = this.getPost(id);
        this.postRepo.delete(post);
    }
}