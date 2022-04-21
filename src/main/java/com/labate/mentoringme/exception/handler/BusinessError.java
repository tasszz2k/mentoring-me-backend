package com.labate.mentoringme.exception.handler;

import com.labate.mentoringme.exception.http.ResponseError;
import lombok.Getter;

@Getter
public enum BusinessError implements ResponseError {
  USER_ALREADY_FEEDBACK_MENTOR(500001, "User already feedback mentor"), USER_ALREADY_LIKE_MENTOR(
      500002, "User already like mentor"), USER_ALREADY_LIKE_QUIZ(500003,
          "User already like quiz"), USER_CAN_NOT_CREATE_FEEDBACK(500004,
              "Only students who have studied can give feedback");

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
