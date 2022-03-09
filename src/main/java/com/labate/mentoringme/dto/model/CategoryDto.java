package com.labate.mentoringme.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CategoryDto {

  private Long id;
  @NotNull private Long parentCategoryId;

  @NotBlank private String name;
  @NotBlank private String code;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<CategoryDto> subCategories;
}
