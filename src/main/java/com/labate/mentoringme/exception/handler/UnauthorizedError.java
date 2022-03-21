package com.labate.mentoringme.exception.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.labate.mentoringme.exception.http.ResponseError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum UnauthorizedError implements ResponseError {
  AUTHENTICATION_IS_REQUIRED(4010000, "Null or not supported authentication"),
  FORBIDDEN_ACCESS_TOKEN(4010001, "Access token has been forbidden due to user has logged out"),
  FORBIDDEN_REFRESH_TOKEN(4010002, "Refresh token has been forbidden: {0}"),
  LOGIN_FAIL_WARNING_BEFORE_BLOCK(
      4010003, "Login fail, the account will be blocked if login invalid more than 5 times"),
  LOGIN_FAIL(4010004, "Login fail, please try again. {0} times"),
  LOGIN_FAIL_BLOCK_ACCOUNT(
      4010005, "The account is temporarily blocked due to invalid login more than 5 times"),
  ;

  private final int code;
  private final String message;

  UnauthorizedError(int code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public String getName() {
    return this.name();
  }

  public int getStatus() {
    return HttpStatus.UNAUTHORIZED.value();
  }

  @Override
  public Integer getCode() {
    return code;
  }
}
