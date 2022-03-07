package com.labate.mentoringme.exception.handler;

import com.labate.mentoringme.exception.http.ResponseError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum InvalidInputError implements ResponseError {
  PERMISSION_IDENTITY_NOT_FOUND(4000001, "Permission identity not found: {0}"),
  POLICY_NOT_FOUND(4000002, "Policy not found: {0}"),
  // USER_NOT_FOUND(4000003, "User not found: {0}"),
  GROUP_NOT_FOUND(4000004, "Group not found: {0}"),
  TENANT_EXISTED(4000005, "Tenant existed: {0}"),
  TENANT_NOT_EXISTED(4000006, "Tenant not existed: {0}"),
  RESOURCE_SCOPE_NOT_FOUND(4000007, "Resource scopes not existed: {0}"),
  NOT_SUPPORTED_PASSWORD_CHANGE(
      4000008, "Only user authentication is allowed to change password: {0}"),
  EMAIL_NOT_EXISTED(4000009, "Email not existed: {0}"),
  NOT_SUPPORTED_PROFILE_CHANGE(
      4000010, "Only user authentication is allowed to change profile: {0}"),
  NOT_SUPPORTED_LOGOUT(4000011, "Only user authentication is allowed to logout: {0}"),
  ORG_UNIT_NOT_FOUND(4000012, "Organization unit not existed: {0}"),
  RESOURCE_NOT_FOUND(4000013, "Resource not found: {0}"),
  NEW_PASSWORD_SAME_OLD(4000014, "New password must be different current password"),
  PASSWORD_NOT_VALID(4000015, "Password is not valid"),
  WRONG_CODE(4000016, "Wrong code"),
  LOGIN_STATUS_NOT_VALID(4000017, "login status not valid: {0}"),
  USER_STATUS_NOT_VALID(4000018, "User status not valid: {0}"),
  FILE_NOT_EXISTED(4000019, "File not existed: {0}"),
  USER_AVATAR_IMAGE_UUID_FORMAT(400020, "User avatar image uuid not format");

  private final Integer code;
  private final String message;

  InvalidInputError(Integer code, String message) {
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
    return HttpStatus.BAD_REQUEST.value();
  }

  public Integer getCode() {
    return code;
  }

  public String getCodeString() {
    return code.toString();
  }
}
