package com.labate.mentoringme.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.labate.mentoringme.model.MentorshipRequest;
import com.labate.mentoringme.model.Role;
import lombok.Data;

@Data
public class CreateMentorshipRequestRq {
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  private CreateMentorshipRequest mentorship;
  private Long requesterId;
  private Long assigneeId;
  private Role RequesterRole;
  private MentorshipRequest.Status status;
}
