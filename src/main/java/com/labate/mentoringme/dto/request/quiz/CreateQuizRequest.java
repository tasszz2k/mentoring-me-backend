package com.labate.mentoringme.dto.request.quiz;

import java.util.List;
import com.labate.mentoringme.dto.model.QuestionDto;
import com.labate.mentoringme.dto.model.QuizCategoryDto;
import lombok.Data;

@Data
public class CreateQuizRequest {
  private String title;
  private String description;
  private Integer numberOfQuestion;
  private Integer time;
  private Boolean isDraft;
  private Long createdBy;
  private List<QuizCategoryDto> categories;
  private List<QuestionDto> questions;
}
