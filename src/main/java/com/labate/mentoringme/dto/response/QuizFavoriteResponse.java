package com.labate.mentoringme.dto.response;

import lombok.Data;

@Data
public class QuizFavoriteResponse extends QuizResponse {
  private Boolean isLiked;
}
