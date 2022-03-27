package com.labate.mentoringme.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class FeedbackOverviewResponse {

  private FeedbackResponse myFeedback;
  private final Double overallRating;
}
