package com.labate.mentoringme.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseResponseEntity {
  public static ResponseEntity<?> ok(Object data, String message) {
    return ResponseEntity.ok(ServiceResponse.succeed(HttpStatus.OK, data, message));
  }

  public static ResponseEntity<?> ok(Object data) {
    return ResponseEntity.ok(ServiceResponse.succeed(HttpStatus.OK, data, null));
  }
}
