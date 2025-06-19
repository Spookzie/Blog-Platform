package com.spookzie.Blog_Platform.mappers;

import com.spookzie.Blog_Platform.domain.CreatePostRequest;
import com.spookzie.Blog_Platform.domain.UpdatePostRequest;
import com.spookzie.Blog_Platform.domain.dtos.CreatePostRequestDto;
import com.spookzie.Blog_Platform.domain.dtos.PostDto;
import com.spookzie.Blog_Platform.domain.dtos.UpdatePostRequestDto;
import com.spookzie.Blog_Platform.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper
{
    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDto toDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto create_post_request_dto);

    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto update_post_request_dto);
}