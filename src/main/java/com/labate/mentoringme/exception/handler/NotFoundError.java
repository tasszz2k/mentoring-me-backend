package com.labate.mentoringme.exception.handler;

import com.labate.mentoringme.exception.http.ResponseError;
import lombok.Getter;

@Getter
public enum NotFoundError implements ResponseError {
  PI_NOT_FOUND(4040001, "Permission id not found: {0}"),
  USER_NOT_FOUND(4040002, "User not found: {0}"),
  CLIENT_NOT_FOUND(4040003, "Client not found: {0}"),
  ORG_UNIT_NOT_FOUND(4040004, "Organization unit not found: {0}"),
  RESOURCE_NOT_FOUND(4040005, "Resource not found: {0}"),
  GROUP_NOT_FOUND(4040006, "Group not found: {0}"),
  TENANT_NOT_FOUND(4040007, "Tenant not found: {0}"),
  RESOURCE_SCOPE_NOT_FOUND(4040008, "Resource scope not found: {0}"),
  CATEGORY_NOT_FOUND(4040009, "Category not found: {0}"),
  QUIZ_NOT_FOUND(4040010, "Quiz not found: {0}"),
  MENTORSHIP_NOT_FOUND(4040011, "Mentorship not found: {0}"),
  POST_NOT_FOUND(4040012, "Post not found: {0}"),
  MENTORSHIP_REQUEST_NOT_FOUND(4040013, "Mentorship request not found: {0}"),
  TIMETABLE_NOT_FOUND(4040014, "Timetable not found: {0}"),
  ;

  private final Integer code;
  private final String message;

  NotFoundError(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public String getName() {
    return name();
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public int getStatus() {
    return 404;
  }

  public Integer getCode() {
    return code;
  }
}
