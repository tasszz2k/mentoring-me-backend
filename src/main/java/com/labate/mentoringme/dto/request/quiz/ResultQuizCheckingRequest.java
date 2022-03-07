package com.labate.mentoringme.dto.request.quiz;

import com.labate.mentoringme.dto.UserSelectionDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ResultQuizCheckingRequest {
  @NotNull private Long quizId;
  @NotNull private List<UserSelectionDto> userSelection;
}
