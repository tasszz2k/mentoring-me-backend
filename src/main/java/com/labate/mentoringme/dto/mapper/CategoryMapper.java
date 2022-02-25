package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.dto.model.CategoryDto;
import com.labate.mentoringme.model.Category;
import com.labate.mentoringme.util.ObjectMapperUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {

  public static CategoryDto toDto(Category entity) {
    return ObjectMapperUtils.map(entity, CategoryDto.class);
  }

  public static Category toEntity(CategoryDto dto) {
    return ObjectMapperUtils.map(dto, Category.class);
  }

  public static List<CategoryDto> toDtos(Collection<Category> entities) {
    return entities == null
        ? null
        : entities.stream().map(CategoryMapper::toDto).collect(Collectors.toList());
  }

  public static List<Category> toEntities(Collection<CategoryDto> dtos) {
    return dtos == null
        ? null
        : dtos.stream().map(CategoryMapper::toEntity).collect(Collectors.toList());
  }
}
