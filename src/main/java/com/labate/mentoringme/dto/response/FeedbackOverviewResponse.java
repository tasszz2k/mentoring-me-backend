package com.labate.mentoringme.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class FeedbackOverviewResponse {

  private FeedbackResponse myFeedback;
  private Double overallRating;
  private Integer numberOfFeedback;
  private Integer numberOfOneRating;
  private Integer numberOfTwoRating;
  private Integer numberOfThreeRating;
  private Integer numberOfFourRating;
  private Integer numberOfFiveRating;
}
