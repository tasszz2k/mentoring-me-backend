// package com.labate.mentoringme.dto.response;
//
// import com.fasterxml.jackson.annotation.JsonInclude;
// import com.nimbusds.oauth2.sdk.http.HTTPResponse;
// import lombok.Value;
// import org.springframework.http.HttpStatus;
//
// @JsonInclude(JsonInclude.Include.NON_NULL)
// @Value
// public class ApiResponse {
//   int code;
//   Object data;
//   String message;
//
//   public static ApiResponse success(Object data, String message) {
//     return new ApiResponse(HttpStatus.OK.value(), data, message);
//   }
//
//   public static ApiResponse fail(Object data, String message) {
//     return new ApiResponse(HttpStatus.OK.value(), data, message);
//   }
//
//   public static <T> ApiResponse<T> succeed(HttpStatus status, T datta) {
//     return new ApiResponse<>(status.value(), null, datta);
//   }
//
//   public static <T> ApiResponse<T> succeed(HttpStatus status, String message, T datta) {
//     return new ApiResponse<>(status.value(), message, datta);
//   }
// }
