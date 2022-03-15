package com.labate.mentoringme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MentorshipNotFoundException extends RuntimeException {

  public MentorshipNotFoundException(String message) {
    super(message);
  }

  public MentorshipNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
