package com.labate.mentoringme.exception.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.labate.mentoringme.exception.http.ResponseError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum AccessDeniedError implements ResponseError {
  ACCESS_DENIED(4030000, "You don't have permission to access this"),
  // NOT_SUPPORTED_AUTHENTICATION(4030001, "Your authentication has not been supported yet: {0}"),
  // NOT_PERMITTED_ACCESS(4030002, "Need permission to access: {0}"),
  ;

  private final int code;
  private final String message;

  AccessDeniedError(int code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public String getName() {
    return this.name();
  }

  public int getStatus() {
    return HttpStatus.FORBIDDEN.value();
  }

  @Override
  public Integer getCode() {
    return code;
  }
}
