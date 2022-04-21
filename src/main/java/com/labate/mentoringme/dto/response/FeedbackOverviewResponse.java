package com.labate.mentoringme.dto.response;

import com.labate.mentoringme.dto.model.FeedbackDto;
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

  private FeedbackDto myFeedback;
  private Double overallRating;
  private Integer numberOfFeedback;
  private Double proportionOfOneRating;
  private Double proportionOfTwoRating;
  private Double proportionOfThreeRating;
  private Double proportionOfFourRating;
  private Double proportionOfFiveRating;
}
