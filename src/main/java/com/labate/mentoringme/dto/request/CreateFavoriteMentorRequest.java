package com.labate.mentoringme.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFavoriteMentorRequest {

  @NotNull
  private Long mentorId;
}
