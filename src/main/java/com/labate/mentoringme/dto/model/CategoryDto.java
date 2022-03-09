package com.labate.mentoringme.dto.model;

import lombok.Data;

@Data
public class CategoryDto {

  private Long id;
  private Long parentCategoryId;
  private String name;
  private String code;
}
