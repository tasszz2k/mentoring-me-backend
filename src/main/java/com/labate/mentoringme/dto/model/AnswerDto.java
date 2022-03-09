package com.labate.mentoringme.dto.model;

import lombok.Data;

@Data
public class AnswerDto {
  private Long id;
  private String answer;
  private Boolean isCorrect;
}
