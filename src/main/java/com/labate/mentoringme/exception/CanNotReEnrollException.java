package com.labate.mentoringme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CanNotReEnrollException extends RuntimeException {

  public CanNotReEnrollException(String message) {
    super(message);
  }

  public CanNotReEnrollException(String message, Throwable cause) {
    super(message, cause);
  }
}
