package com.labate.mentoringme.dto.response;

import lombok.Data;

@Data
public class QuizResultResponse {
  private Integer numberOfTrue;
  private Integer numberOfFalse;
  private Integer numberOfQuestion;
  private Integer score;
}
