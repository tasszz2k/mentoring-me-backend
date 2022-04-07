package com.labate.mentoringme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UserAlreadyLikeQuizException extends RuntimeException {
  public UserAlreadyLikeQuizException(String message) {
    super(message);
  }

  public UserAlreadyLikeQuizException(String message, Throwable cause) {
    super(message, cause);
  }
}
