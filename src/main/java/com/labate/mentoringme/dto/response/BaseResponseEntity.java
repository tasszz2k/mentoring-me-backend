package com.labate.mentoringme.dto.response;

import org.springframework.http.ResponseEntity;

public class BaseResponseEntity {
  public static ResponseEntity<?> ok(Object data, String message) {
    return ResponseEntity.ok(ApiResponse.success(data, message));
  }

  public static ResponseEntity<?> fail(Object data, String message) {
    return ResponseEntity.ok(ApiResponse.fail(data, message));
  }

  public static ResponseEntity<?> ok(Object data) {
    return ResponseEntity.ok(ApiResponse.success(data, null));
  }
}
