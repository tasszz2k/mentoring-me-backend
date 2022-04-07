package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.dto.model.ShiftDto;
import com.labate.mentoringme.model.Shift;
import com.labate.mentoringme.util.ObjectMapperUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShiftMapper {

  public static ShiftDto toDto(Shift entity) {
    if (entity == null) {
      return null;
    }
    return ObjectMapperUtils.map(entity, ShiftDto.class);
  }

  public static Shift toEntity(ShiftDto dto) {
    if (dto == null) {
      return null;
    }

    return ObjectMapperUtils.map(dto, Shift.class);
  }

  public static List<ShiftDto> toDtos(Collection<Shift> entities) {

    List<ShiftDto> dtos;
    if (entities == null) {
      dtos = Collections.emptyList();
    } else {
      var sortedEntities =
          entities.stream().sorted(Comparator.comparing(Shift::getId)).collect(Collectors.toList());
      dtos = sortedEntities.stream().map(ShiftMapper::toDto).collect(Collectors.toList());
    }
    return dtos;
  }

  public static List<Shift> toEntities(Collection<ShiftDto> dtos) {
    return dtos == null
        ? null
        : dtos.stream().map(ShiftMapper::toEntity).collect(Collectors.toList());
  }
}
