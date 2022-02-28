package com.labate.mentoringme.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Value
public class ApiResponse {
  Boolean success;
  Object data;
  String message;
  Metadata metadata;

  public static ApiResponse success(Object data, String message) {
    return new ApiResponse(true, data, message, null);
  }

  public static ApiResponse success(Object data, Metadata metadata) {
    return new ApiResponse(true, data, null, metadata);
  }

  public static ApiResponse fail(Object data, String message) {
    return new ApiResponse(false, data, message, null);
  }
}
