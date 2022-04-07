package com.labate.mentoringme.dto.response;

import com.labate.mentoringme.constant.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QuizOverviewResponse extends QuizResponse {
  private Boolean isLiked;
  private String imageUrl;
  private UserRole role;
}
