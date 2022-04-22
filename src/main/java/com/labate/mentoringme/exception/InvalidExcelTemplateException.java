package com.labate.mentoringme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidExcelTemplateException extends RuntimeException {

  public InvalidExcelTemplateException(String message) {
    super(message);
  }

  public InvalidExcelTemplateException(String message, Throwable cause) {
    super(message, cause);
  }
}
