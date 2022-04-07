package com.labate.mentoringme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FavoriteQuizNotFoundException extends RuntimeException {

  public FavoriteQuizNotFoundException(String message) {
    super(message);
  }

  public FavoriteQuizNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
