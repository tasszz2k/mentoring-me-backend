package com.labate.mentoringme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCategoryException extends RuntimeException {

  public InvalidCategoryException(String message) {
    super(message);
  }

  public InvalidCategoryException(String message, Throwable cause) {
    super(message, cause);
  }
}
