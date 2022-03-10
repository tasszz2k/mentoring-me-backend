package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.dto.model.MentorshipRequestDto;
import com.labate.mentoringme.model.Class;
import com.labate.mentoringme.service.mentorshiprequest.MentorshipRequestService;
import com.labate.mentoringme.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MentorshipRequestMapper {

  private static MentorshipRequestService mentorshipRequestService;

  @Autowired
  public MentorshipRequestMapper(MentorshipRequestService mentorshipRequestService) {
    MentorshipRequestMapper.mentorshipRequestService = mentorshipRequestService;
  }

  public static MentorshipRequestDto toDto(Class entity) {
    if (entity == null) {
      return null;
    }
    var dto = ObjectMapperUtils.map(entity, MentorshipRequestDto.class);
    return dto;
  }

  // public static Class toEntity(MentorshipRequestDto dto) {
  //   if (dto == null) {
  //     return null;
  //   }
  //   var entity = ObjectMapperUtils.map(dto, Class.class);
  //   var parentClassId = dto.getParentClassId();
  //
  //   var parentClass = mentorshipRequestService.findById(parentClassId);
  //   if (parentClass != null) {
  //     entity.setParentClass(parentClass);
  //   }
  //
  //   return entity;
  // }
  //
  public static List<MentorshipRequestDto> toDtos(Collection<Class> entities) {

    List<MentorshipRequestDto> dtos;
    if (entities == null) {
      dtos = null;
    } else {
      dtos = entities.stream().map(MentorshipRequestMapper::toDto).collect(Collectors.toList());
    }
    return dtos;
  }

  // public static List<Class> toEntities(Collection<MentorshipRequestDto> dtos) {
  //   return dtos == null
  //       ? null
  //       : dtos.stream().map(MentorshipRequestMapper::toEntity).collect(Collectors.toList());
  // }
}
