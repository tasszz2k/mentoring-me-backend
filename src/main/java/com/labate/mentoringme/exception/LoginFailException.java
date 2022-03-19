package com.labate.mentoringme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class LoginFailException extends AuthenticationException {

  public LoginFailException(String message) {
    super(message);
  }

  public LoginFailException(String message, Throwable cause) {
    super(message, cause);
  }
}
