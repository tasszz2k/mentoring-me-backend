package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.dto.model.MentorVerificationDto;
import com.labate.mentoringme.model.MentorVerification;
import com.labate.mentoringme.service.category.CategoryService;
import com.labate.mentoringme.service.user.UserService;
import com.labate.mentoringme.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MentorVerificationMapper {
  private static UserService userService;

  @Autowired
  public MentorVerificationMapper(CategoryService categoryService, UserService userService) {
    MentorVerificationMapper.userService = userService;
  }

  public static MentorVerificationDto toDto(MentorVerification entity) {
    if (entity == null) {
      return null;
    }

    var dto = ObjectMapperUtils.map(entity, MentorVerificationDto.class);
    if (entity.getMentorId() != null) {
      var mentor = userService.findBasicUserInfoByUserId(entity.getMentorId());
      if (mentor != null) {
        dto.setMentor(mentor);
      }
    }
    if (entity.getModeratorId() != null) {
      var mod = userService.findBasicUserInfoByUserId(entity.getModeratorId());
      if (mod != null) {
        dto.setModerator(mod);
      }
    }
    return dto;
  }

  public static List<MentorVerificationDto> toDtos(Collection<MentorVerification> entities) {
    return entities == null
        ? null
        : entities.stream().map(MentorVerificationMapper::toDto).collect(Collectors.toList());
  }
}
