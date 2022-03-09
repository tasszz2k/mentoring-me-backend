package com.labate.mentoringme.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class CategoryDto {

  private Long id;
  private Long parentCategoryId;
  private String name;
  private String code;
}
