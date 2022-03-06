package com.labate.mentoringme.dto.request.quiz;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.labate.mentoringme.dto.model.QuestionDto;
import com.labate.mentoringme.dto.model.QuizCategoryDto;
import lombok.Data;

@Data
public class UpdateQuizRequest {
  @NotNull
  private Long id;
  @NotBlank
  private String title;
  private String description;
  @NotNull
  private Integer numberOfQuestion;
  private Integer time;
  private Boolean isDraft;
  @NotNull
  private Long modifiedBy;
  private List<QuizCategoryDto> categories;
  private List<QuestionDto> questions;
}
