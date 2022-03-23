package com.labate.mentoringme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReportNotFoundException extends RuntimeException {
  public ReportNotFoundException(String message) {
    super(message);
  }

  public ReportNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
