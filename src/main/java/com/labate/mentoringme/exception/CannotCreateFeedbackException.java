package com.labate.mentoringme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CannotCreateFeedbackException extends RuntimeException {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public CannotCreateFeedbackException(String message) {
    super(message);
  }

  public CannotCreateFeedbackException(String message, Throwable cause) {
    super(message, cause);
  }
}
