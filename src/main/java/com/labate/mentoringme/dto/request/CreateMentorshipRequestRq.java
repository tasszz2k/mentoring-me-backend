package com.labate.mentoringme.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.labate.mentoringme.constant.UserRole;
import com.labate.mentoringme.model.MentorshipRequest;
import com.labate.mentoringme.validator.AcceptableRoles;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateMentorshipRequestRq {
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @NotNull private CreateMentorshipRequest mentorship;
  @NotNull private Long assigneeId;
  @NotNull private Long approverId;
  private String content;

  @NotNull
  @AcceptableRoles(
      roles = {UserRole.ROLE_USER, UserRole.ROLE_MENTOR},
      message = "Only users and mentors can be invited to mentorship")
  private UserRole assigneeRole = UserRole.ROLE_MENTOR;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private MentorshipRequest.Status status = MentorshipRequest.Status.ON_GOING;
}
