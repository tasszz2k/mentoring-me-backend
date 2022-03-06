package com.labate.mentoringme.dto.request.quiz;

import java.util.List;
import javax.validation.constraints.NotNull;
import com.labate.mentoringme.dto.UserSelectionDto;
import lombok.Data;

@Data
public class ResultQuizCheckingRequest {
  @NotNull
  private Long quizId;
  @NotNull
  private Long userId;
  @NotNull
  private List<UserSelectionDto> userSelection;
}
