package com.labate.mentoringme.dto.model;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {
  private Long id;
  private String question;
  private String description;
  private Boolean isMultipleChoice;
  private Boolean isDeleted = false;
  private List<AnswerDto> answers;
}
