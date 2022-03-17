package com.labate.mentoringme.dto.request;

import com.labate.mentoringme.model.MentorVerification;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class VerifyMentorRequest {
  @NotNull Long mentorId;
  @NotNull MentorVerification.Status status;
}
