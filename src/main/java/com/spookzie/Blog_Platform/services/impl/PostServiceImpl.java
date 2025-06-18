package com.spookzie.Blog_Platform.services.impl;

import com.spookzie.Blog_Platform.domain.PostStatus;
import com.spookzie.Blog_Platform.domain.entities.Category;
import com.spookzie.Blog_Platform.domain.entities.Post;
import com.spookzie.Blog_Platform.domain.entities.Tag;
import com.spookzie.Blog_Platform.domain.entities.User;
import com.spookzie.Blog_Platform.repositories.PostRepository;
import com.spookzie.Blog_Platform.services.CategoryService;
import com.spookzie.Blog_Platform.services.PostService;
import com.spookzie.Blog_Platform.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService
{
    private final PostRepository postRepo;
    private final CategoryService categoryService;
    private final TagService tagService;


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
    public List<Post> getDraftPosts(User user)
    {
        return this.postRepo.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }
}