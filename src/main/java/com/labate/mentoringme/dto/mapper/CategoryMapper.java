package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.dto.model.CategoryDto;
import com.labate.mentoringme.model.Category;
import com.labate.mentoringme.service.category.CategoryService;
import com.labate.mentoringme.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

  private static CategoryService categoryService;

  @Autowired
  public CategoryMapper(CategoryService categoryService) {
    CategoryMapper.categoryService = categoryService;
  }

  public static CategoryDto toDto(Category entity) {
    var dto = ObjectMapperUtils.map(entity, CategoryDto.class);
    var parent = entity.getParentCategory();
    var parentCategoryId = parent == null ? null : parent.getId();
    dto.setParentCategoryId(parentCategoryId);
    return dto;
  }

  public static Category toEntity(CategoryDto dto) {
    var entity = ObjectMapperUtils.map(dto, Category.class);
    var parentCategoryId = dto.getParentCategoryId();

    var parentCategory = categoryService.findById(parentCategoryId);
    if (parentCategory != null) {
      entity.setParentCategory(parentCategory);
    }

    return entity;
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
