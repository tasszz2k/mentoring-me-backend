package com.labate.mentoringme.dto.model;

import java.util.List;
import lombok.Data;

@Data
public class QuestionDto {
  private Long id;
  private Long quizId;
  private String question;
  private Boolean isMultipleChoice;
  private List<AnswerDto> answers;
}
