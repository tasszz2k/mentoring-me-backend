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
  private Double proportionOfOneRating;
  private Double proportionOfTwoRating;
  private Double proportionOfThreeRating;
  private Double proportionOfFourRating;
  private Double proportionOfFiveRating;
}
