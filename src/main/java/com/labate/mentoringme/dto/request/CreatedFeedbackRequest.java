package com.labate.mentoringme.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatedFeedbackRequest {
  private Long toUserId;

  private Integer rating;

  private String comment;
}
