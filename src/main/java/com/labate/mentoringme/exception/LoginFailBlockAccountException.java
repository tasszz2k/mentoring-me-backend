package com.labate.mentoringme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class LoginFailBlockAccountException extends AuthenticationException {

  public LoginFailBlockAccountException(String message) {
    super(message);
  }

  public LoginFailBlockAccountException(String message, Throwable cause) {
    super(message, cause);
  }
}
