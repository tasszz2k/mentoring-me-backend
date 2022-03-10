package com.labate.mentoringme.exception.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.labate.mentoringme.exception.http.ResponseError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum BadRequestError implements ResponseError {
  INVALID_INPUT(4000000, "Invalid input"),
  INVALID_ACCEPT_LANGUAGE(4000001, "Invalid value for request header Accept-Language: {0}"),
  MISSING_PATH_VARIABLE(4000002, "Missing path variable"), // MissingPathVariable
  CLASS_HAS_BEGUN(4000003, "Class has begun: {0}"),
  CANNOT_RE_ENROLL(4000004, "You cannot re-enroll in this class: {0}"),
  ;

  private final Integer code;
  private final String message;

  BadRequestError(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public String getName() {
    return this.name();
  }

  @Override
  public int getStatus() {
    return HttpStatus.BAD_REQUEST.value();
  }
}
