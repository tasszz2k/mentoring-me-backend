package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.model.PostDto;
import com.labate.mentoringme.dto.request.CreatePostRequest;
import com.labate.mentoringme.model.Post;
import com.labate.mentoringme.util.ObjectMapperUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MentorshipRequestMapper {

  public static PostDto toDto(Post entity) {
    if (entity == null) {
      return null;
    }

    return ObjectMapperUtils.map(entity, PostDto.class);
  }

  public static Post toEntity(PostDto dto) {
    if (dto == null) {
      return null;
    }

    return ObjectMapperUtils.map(dto, Post.class);
  }

  public static Post toEntity(CreatePostRequest dto, LocalUser localUser) {
    if (dto == null) {
      return null;
    }

    Post post = ObjectMapperUtils.map(dto, Post.class);
    return post;
  }

  public static List<PostDto> toDtos(Collection<Post> entities) {
    return entities == null
        ? null
        : entities.stream().map(MentorshipRequestMapper::toDto).collect(Collectors.toList());
  }

  public static List<Post> toEntities(Collection<PostDto> dtos) {
    return dtos == null
        ? null
        : dtos.stream().map(MentorshipRequestMapper::toEntity).collect(Collectors.toList());
  }
}

