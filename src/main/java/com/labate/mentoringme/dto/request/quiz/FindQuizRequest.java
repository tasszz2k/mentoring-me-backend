package com.labate.mentoringme.dto.request.quiz;

import java.util.List;
import lombok.Data;

@Data
public class FindQuizRequest {
  private Long userId;
  private List<Long> categoryIds;
  private String keyword;
  private Integer minNumberOfQuestion;
  private Integer maxNumberOfQuestion;
  private Integer minTime;
  private Integer maxTime;
}
