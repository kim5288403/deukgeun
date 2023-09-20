package com.example.deukgeun.trainer.infrastructure.persistence.mapper;

import com.example.deukgeun.trainer.domain.model.entity.Post;
import com.example.deukgeun.trainer.infrastructure.persistence.model.entity.PostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Named("toPost")
    Post toPost(PostEntity postEntity);

    @Named("toPostEntity")
    PostEntity toPostEntity(Post post);
}
