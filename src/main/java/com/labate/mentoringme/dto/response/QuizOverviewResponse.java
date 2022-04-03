package com.labate.mentoringme.dto.response;

import com.labate.mentoringme.constant.UserRole;
import lombok.Data;

@Data
public class QuizOverviewResponse extends QuizResponse {
  private Boolean isLiked;
  private String imageUrl;
  private UserRole role;
}
