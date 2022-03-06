package com.labate.mentoringme.dto.request.quiz;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.labate.mentoringme.dto.model.QuestionDto;
import com.labate.mentoringme.dto.model.QuizCategoryDto;
import lombok.Data;

@Data
public class CreateQuizRequest {

  @NotBlank
  private String title;
  private String description;
  @NotNull
  private Integer numberOfQuestion;
  @NotNull
  private Integer time;
  private Boolean isDraft;
  @NotNull
  private Long createdBy;
  @NotNull
  private List<QuizCategoryDto> categories;
  private List<QuestionDto> questions;
}
