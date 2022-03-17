package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.dto.model.CategoryDto;
import com.labate.mentoringme.exception.CategoryNotFoundException;
import com.labate.mentoringme.model.Category;
import com.labate.mentoringme.service.category.CategoryService;
import com.labate.mentoringme.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

  private static CategoryService categoryService;

  @Autowired
  public CategoryMapper(CategoryService categoryService) {
    CategoryMapper.categoryService = categoryService;
  }

  public static CategoryDto toDto(Category entity) {
    if (entity == null) {
      return null;
    }
    return ObjectMapperUtils.map(entity, CategoryDto.class);
  }

  public static Category toEntity(CategoryDto dto) {
    if (dto == null) {
      return null;
    }
    var entity = ObjectMapperUtils.map(dto, Category.class);
    var parentCategoryId = dto.getParentCategoryId();

    var parentCategory = categoryService.findById(parentCategoryId);
    if (parentCategory == null) {
      throw new CategoryNotFoundException("Parent category not found, id: " + parentCategoryId);
    }

    return entity;
  }

  public static List<CategoryDto> toDtos(Collection<Category> entities) {

    List<CategoryDto> dtos;
    if (entities == null) {
      dtos = null;
    } else {
      var dtoMap = new HashMap<Long, CategoryDto>();
      entities.forEach(
          entity -> {
            if (entity.getParentCategoryId() == null) {
              dtoMap.put(entity.getId(), toDto(entity));
              return;
            }
            var parent = dtoMap.get(entity.getParentCategoryId());
            if (parent != null) {
              parent.getSubCategories().add(toDto(entity));
            } else {
              dtoMap.put(entity.getId(), toDto(entity));
            }
          });
      dtos = new ArrayList<>(dtoMap.values());
      dtos.sort(Comparator.comparing(CategoryDto::getId));
      dtos.forEach(dto -> dto.getSubCategories().sort(Comparator.comparing(CategoryDto::getId)));
    }
    return dtos;
  }

  public static List<Category> toEntities(Collection<CategoryDto> dtos) {
    return dtos == null
        ? null
        : dtos.stream().map(CategoryMapper::toEntity).collect(Collectors.toList());
  }
}
