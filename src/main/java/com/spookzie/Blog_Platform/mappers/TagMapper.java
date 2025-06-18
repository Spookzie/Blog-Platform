package com.spookzie.Blog_Platform.mappers;

import com.spookzie.Blog_Platform.domain.PostStatus;
import com.spookzie.Blog_Platform.domain.dtos.TagDto;
import com.spookzie.Blog_Platform.domain.entities.Post;
import com.spookzie.Blog_Platform.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper
{
    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    TagDto toDto(Tag tag);

    @Named("calculatePostCount")
    default long calculatePostCount(Set<Post> posts)
    {
        if(posts == null)
            return 0;

        return posts.stream()
                .filter(post -> post.getStatus().equals(PostStatus.PUBLISHED))
                .count();
    }
}