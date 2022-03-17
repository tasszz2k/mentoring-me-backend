package com.labate.mentoringme.dto.request.quiz;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddFavoriteQuizRequest {
  @NotNull
  private Long quizId;
}
