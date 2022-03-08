package com.labate.mentoringme.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.labate.mentoringme.constant.UserRole;
import lombok.Value;

import java.util.List;

@Value
public class FindUsersRequest {
  UserRole role;
  List<Long> categoryIds;
  List<Long> addressIds;

  @JsonIgnore // FIXME: config swagger to hidden this field
  public String getRoleName() {
    return role != null ? role.name() : null;
  }
}
