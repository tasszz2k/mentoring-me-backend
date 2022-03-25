package com.labate.mentoringme.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FeedbackResponse {
  private PageResponse pageResponse;
  private Double overallRating;
}
