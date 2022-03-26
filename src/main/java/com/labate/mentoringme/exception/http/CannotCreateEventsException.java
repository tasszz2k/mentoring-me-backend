package com.labate.mentoringme.exception.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotCreateEventsException extends RuntimeException {

  public CannotCreateEventsException(String message) {
    super(message);
  }

  public CannotCreateEventsException(String message, Throwable cause) {
    super(message, cause);
  }
}
