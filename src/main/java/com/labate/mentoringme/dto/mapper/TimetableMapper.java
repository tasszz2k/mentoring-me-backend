package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.dto.model.TimetableDto;
import com.labate.mentoringme.model.Timetable;
import com.labate.mentoringme.service.user.UserService;
import com.labate.mentoringme.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TimetableMapper {

  private static UserService userService;

  @Autowired
  public TimetableMapper(UserService userService) {
    TimetableMapper.userService = userService;
  }

  public static TimetableDto toDto(Timetable entity) {
    var dto = ObjectMapperUtils.map(entity, TimetableDto.class);
    var user = userService.findUserById(entity.getUserId()).orElse(null);
    assert user != null;
    dto.setUser(UserMapper.buildUserInfo(user));
    return dto;
  }

  public static Timetable toEntity(TimetableDto dto) {
    return ObjectMapperUtils.map(dto, Timetable.class);
  }

  public static List<TimetableDto> toDtos(Collection<Timetable> entities) {
    return entities == null
        ? null
        : entities.stream().map(TimetableMapper::toDto).collect(Collectors.toList());
  }

  public static List<Timetable> toEntities(Collection<TimetableDto> dtos) {
    return dtos == null
        ? null
        : dtos.stream().map(TimetableMapper::toEntity).collect(Collectors.toList());
  }
}
