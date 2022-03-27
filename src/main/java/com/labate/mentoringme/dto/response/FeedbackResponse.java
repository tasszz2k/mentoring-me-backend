package com.labate.mentoringme.dto.response;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackResponse {
  private String fullName;
  private String imageUrl;
  private Integer rating;
  private String comment;
  private Date created;
}
