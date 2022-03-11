package com.labate.mentoringme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotLikeOrUnlikeException extends RuntimeException {

  public CannotLikeOrUnlikeException(String message) {
    super(message);
  }

  public CannotLikeOrUnlikeException(String message, Throwable cause) {
    super(message, cause);
  }
}
