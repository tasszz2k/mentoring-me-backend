package com.labate.mentoringme.exception.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.labate.mentoringme.exception.http.ResponseError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum InternalServerError implements ResponseError {
  INTERNAL_SERVER_ERROR("There are somethings wrong {0}"),
  MISSING_SERVLET_REQUEST_PART("There are somethings wrong {0}"),
  ;

  private final String message;

  InternalServerError(String message) {
    this.message = message;
  }

  @Override
  public String getName() {
    return this.name();
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public int getStatus() {
    return HttpStatus.INTERNAL_SERVER_ERROR.value();
  }
}
