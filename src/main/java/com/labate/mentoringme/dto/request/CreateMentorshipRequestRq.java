package com.labate.mentoringme.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.labate.mentoringme.dto.model.ShiftDto;
import lombok.Data;

import javax.validation.Valid;
import java.util.Date;
import java.util.Set;

@Data
public class CreateMentorshipRequestRq {
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

}
