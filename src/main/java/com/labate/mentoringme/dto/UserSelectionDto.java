package com.labate.mentoringme.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserSelectionDto {
  private Long questionId;
  private List<Long> answerIds;
}
