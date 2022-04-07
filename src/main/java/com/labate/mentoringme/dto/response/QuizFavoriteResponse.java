package com.labate.mentoringme.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QuizFavoriteResponse extends QuizResponse {
  private Boolean isLiked;
}
