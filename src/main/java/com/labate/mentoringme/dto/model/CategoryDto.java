package com.labate.mentoringme.dto.model;

import lombok.Data;

@Data
public class CategoryDto {

  private Integer id;
  private CategoryDto parentCategory;
  private String name;
  private String code;
}
