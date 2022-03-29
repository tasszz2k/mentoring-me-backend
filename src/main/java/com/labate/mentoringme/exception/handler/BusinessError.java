package com.labate.mentoringme.exception.handler;

import com.labate.mentoringme.exception.http.ResponseError;
import lombok.Getter;

@Getter
public enum BusinessError implements ResponseError {
  USER_ALREADY_FEEDBACK_MENTOR(500001, "User already feedbacked mentor}"),;

  private final Integer code;
  private final String message;

  BusinessError(Integer code, String message) {
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
    return 500;
  }

  public Integer getCode() {
    return code;
  }

}
