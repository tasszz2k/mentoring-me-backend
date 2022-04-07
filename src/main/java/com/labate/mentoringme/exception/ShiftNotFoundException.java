package com.labate.mentoringme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShiftNotFoundException extends RuntimeException {

  public ShiftNotFoundException(String message) {
    super(message);
  }

  public ShiftNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
