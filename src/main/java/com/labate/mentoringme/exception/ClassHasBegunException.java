package com.labate.mentoringme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClassHasBegunException extends RuntimeException {

  public ClassHasBegunException(String message) {
    super(message);
  }

  public ClassHasBegunException(String message, Throwable cause) {
    super(message, cause);
  }
}
