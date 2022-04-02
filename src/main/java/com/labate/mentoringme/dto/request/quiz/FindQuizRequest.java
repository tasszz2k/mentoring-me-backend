package com.labate.mentoringme.dto.request.quiz;

import lombok.Data;

@Data
public class FindQuizRequest {
  private Long userId;
  private Long categoryId;
  private String keyword;
  private Integer minNumberOfQuestion;
  private Integer maxNumberOfQuestion;
  private Integer minTime;
  private Integer maxTime;
}
