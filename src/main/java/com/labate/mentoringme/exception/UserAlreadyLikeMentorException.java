package com.labate.mentoringme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UserAlreadyLikeMentorException extends RuntimeException {
  public UserAlreadyLikeMentorException(String message) {
    super(message);
  }

  public UserAlreadyLikeMentorException(String message, Throwable cause) {
    super(message, cause);
  }
}
