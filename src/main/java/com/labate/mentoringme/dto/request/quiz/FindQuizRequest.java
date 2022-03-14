package com.labate.mentoringme.dto.request.quiz;

import lombok.Data;

@Data
public class FindQuizRequest {
  private Long userId;
  private Long categoryId;
}
