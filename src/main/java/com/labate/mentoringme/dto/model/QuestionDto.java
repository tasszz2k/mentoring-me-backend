package com.labate.mentoringme.dto.model;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {
  private Long id;
  private String question;
  private Boolean isMultipleChoice;
  private List<AnswerDto> answers;
}
