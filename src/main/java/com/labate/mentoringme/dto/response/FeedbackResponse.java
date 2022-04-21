package com.labate.mentoringme.dto.response;

import java.util.List;
import com.labate.mentoringme.dto.model.FeedbackDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FeedbackResponse {
  private List<FeedbackDto> feedbacks;
  private Boolean isStudied;
}
