package com.labate.mentoringme.dto.request.quiz;

import java.util.Set;
import com.labate.mentoringme.dto.model.QuestionDto;
import lombok.Data;

@Data
public class UpdateQuizDetailRequest {
  private Set<QuestionDto> questions;
}
