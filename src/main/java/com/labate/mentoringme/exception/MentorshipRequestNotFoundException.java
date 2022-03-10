package com.labate.mentoringme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MentorshipRequestNotFoundException extends RuntimeException {

  public MentorshipRequestNotFoundException(String message) {
    super(message);
  }

  public MentorshipRequestNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
