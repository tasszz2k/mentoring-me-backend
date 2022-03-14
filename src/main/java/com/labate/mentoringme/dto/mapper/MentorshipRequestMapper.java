package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.dto.model.MentorshipRequestDto;
import com.labate.mentoringme.dto.request.CreateMentorshipRequestRq;
import com.labate.mentoringme.model.MentorshipRequest;
import com.labate.mentoringme.util.ObjectMapperUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MentorshipRequestMapper {

  public static MentorshipRequestDto toDto(MentorshipRequest entity) {
    if (entity == null) {
      return null;
    }

    return ObjectMapperUtils.map(entity, MentorshipRequestDto.class);
  }

  public static MentorshipRequest toEntity(MentorshipRequestDto dto) {
    if (dto == null) {
      return null;
    }

    return ObjectMapperUtils.map(dto, MentorshipRequest.class);
  }

  public static MentorshipRequest toEntity(CreateMentorshipRequestRq dto) {
    if (dto == null) {
      return null;
    }

    MentorshipRequest entity = ObjectMapperUtils.map(dto, MentorshipRequest.class);
    return entity;
  }

  public static List<MentorshipRequestDto> toDtos(Collection<MentorshipRequest> entities) {
    return entities == null
        ? null
        : entities.stream().map(MentorshipRequestMapper::toDto).collect(Collectors.toList());
  }

  public static List<MentorshipRequest> toEntities(Collection<MentorshipRequestDto> dtos) {
    return dtos == null
        ? null
        : dtos.stream().map(MentorshipRequestMapper::toEntity).collect(Collectors.toList());
  }
}
