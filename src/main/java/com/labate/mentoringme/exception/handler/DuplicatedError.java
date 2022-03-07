package com.labate.mentoringme.exception.handler;

import com.labate.mentoringme.exception.http.ResponseError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DuplicatedError implements ResponseError {
  GROUP_NAME_EXISTED(4090001, "Group name existed: {0}"),
  RESOURCE_ID_EXISTED(4090002, "Resource code and tenant id pair existed: {0} , {1}"),
  RESOURCE_NAME_EXISTED(4090003, "Resource name existed: {0}"),
  USERNAME_EXISTED(4090004, "Username existed: {0}"),
  CLIENT_ID_EXISTED(4090005, "Client id existed: {0}"),
  DATA_POLICY_EXISTED(4090006, "Data policy existed: {0}"),
  EMAIL_EXISTED(4090007, "Email existed: {0}"),
  RESOURCE_SCOPE_EXISTED(4090008, "Resource scope existed: {0} , {1}"),
  ;

  private final Integer code;
  private final String message;

  DuplicatedError(Integer code, String message) {
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
}
