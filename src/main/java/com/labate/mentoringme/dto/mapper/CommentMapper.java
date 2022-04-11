package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.model.CommentDto;
import com.labate.mentoringme.dto.request.CreateCommentRequest;
import com.labate.mentoringme.model.Comment;
import com.labate.mentoringme.service.user.UserService;
import com.labate.mentoringme.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

  private static UserService userService;

  @Autowired
  public CommentMapper(UserService userService) {
    CommentMapper.userService = userService;
  }

  public static CommentDto toDto(Comment entity) {
    if (entity == null) {
      return null;
    }

    var dto = ObjectMapperUtils.map(entity, CommentDto.class);
    if (entity.getCreatedBy() != null) {
      var createdBy = userService.findBasicUserInfoByUserId(entity.getCreatedBy());
      if (createdBy != null) {
        dto.setCreatedBy(createdBy);
      }
    }
    return dto;
  }

  public static Comment toEntity(CommentDto dto) {
    if (dto == null) {
      return null;
    }

    return ObjectMapperUtils.map(dto, Comment.class);
  }

  public static Comment toEntity(CreateCommentRequest dto, LocalUser localUser) {
    if (dto == null) {
      return null;
    }

    return ObjectMapperUtils.map(dto, Comment.class);
  }

  public static List<CommentDto> toDtos(Collection<Comment> entities) {
    return entities == null
        ? null
        : entities.stream().map(CommentMapper::toDto).collect(Collectors.toList());
  }

  public static List<Comment> toEntities(Collection<CommentDto> dtos) {
    return dtos == null
        ? null
        : dtos.stream().map(CommentMapper::toEntity).collect(Collectors.toList());
  }
}
