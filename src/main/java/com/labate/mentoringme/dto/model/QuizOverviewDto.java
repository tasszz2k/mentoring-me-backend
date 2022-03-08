package com.labate.mentoringme.dto.model;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuizOverviewDto {
  private Long id;
  private String title;
  private Integer numberOfQuestion;
  private Integer time;
  private Boolean isDraft;
  @NotNull
  private List<QuizCategoryDto> categories;
}
