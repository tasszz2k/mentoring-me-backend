package com.labate.mentoringme.dto.response;

import lombok.Value;

@Value
public class ApiResponse {
  Boolean success;
  Object data;
  String message;

  public static ApiResponse success(Object data, String message) {
    return new ApiResponse(true, data, message);
  }

  public static ApiResponse fail(Object data, String message) {
    return new ApiResponse(false, data, message);
  }
}
