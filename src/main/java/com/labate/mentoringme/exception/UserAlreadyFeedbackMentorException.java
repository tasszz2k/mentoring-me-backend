package com.labate.mentoringme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UserAlreadyFeedbackMentorException extends RuntimeException {
  public UserAlreadyFeedbackMentorException(String message) {
    super(message);
  }

  public UserAlreadyFeedbackMentorException(String message, Throwable cause) {
    super(message, cause);
  }
}
