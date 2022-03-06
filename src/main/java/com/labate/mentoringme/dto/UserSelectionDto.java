package com.labate.mentoringme.dto;

import java.util.List;
import lombok.Data;

@Data
public class UserSelectionDto {
  private Long questionId;
  private List<Long> answerIds;
}
