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
  @NotNull
  private Integer numberOfQuestion;
  @NotNull
  private Integer time;
  @NotNull
  private Boolean isDraft;
  @NotNull
  private List<QuizCategoryDto> categories;
  private List<QuestionDto> questions;
}
