package com.spookzie.Blog_Platform.mappers;

import com.spookzie.Blog_Platform.domain.PostStatus;
import com.spookzie.Blog_Platform.domain.dtos.CategoryDto;
import com.spookzie.Blog_Platform.domain.dtos.CreateCategoryRequest;
import com.spookzie.Blog_Platform.domain.entities.Category;
import com.spookzie.Blog_Platform.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper
{
    /*  Set the postCount field in the DTO by applying the method calculatePostCount on the posts list of the Category entity   */
    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);


    Category toEntity(CreateCategoryRequest create_category_request);


    /*  Helper Methods  */
    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts)
    {
        if(posts == null)
            return 0;

        return posts.stream()
                .filter(post -> post.getStatus().equals(PostStatus.PUBLISHED))
                .count();
    }
}