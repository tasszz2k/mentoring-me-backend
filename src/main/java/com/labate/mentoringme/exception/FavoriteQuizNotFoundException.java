package com.labate.mentoringme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FavoriteQuizNotFoundException extends RuntimeException {

  private String code;

  public FavoriteQuizNotFoundException(String code, String message) {
    super(message);
    this.code = code;
  }

  public FavoriteQuizNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
