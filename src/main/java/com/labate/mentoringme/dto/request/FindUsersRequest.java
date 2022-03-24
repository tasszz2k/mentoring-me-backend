package com.labate.mentoringme.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.labate.mentoringme.constant.MentorStatus;
import com.labate.mentoringme.constant.UserRole;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class FindUsersRequest {
  UserRole role;
  Boolean enabled;
  List<Long> categoryIds;
  List<Long> addressIds;

  @JsonIgnore // FIXME: config swagger to hidden this field
  public String getRoleName() {
    return role != null ? role.name() : null;
  }

  @JsonIgnore // FIXME: config swagger to hidden this field
  public MentorStatus getMentorStatus() {
    return UserRole.ROLE_MENTOR.equals(role) ? MentorStatus.ACCEPTED : null;
  }
}
