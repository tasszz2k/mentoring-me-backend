package com.labate.mentoringme.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDto {
  @ApiModelProperty(hidden = true)
  private Long id;

  private Long parentCategoryId;

  @NotBlank private String name;
  @NotBlank private String code;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<CategoryDto> subCategories = new ArrayList<>();
}
