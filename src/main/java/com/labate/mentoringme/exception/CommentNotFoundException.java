package com.labate.mentoringme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends RuntimeException {

  public CommentNotFoundException(String message) {
    super(message);
  }

  public CommentNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
