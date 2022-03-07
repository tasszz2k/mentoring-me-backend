package com.labate.mentoringme.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceResponse<T> {
  private int code;
  private T data;
  private String message;

  public static <T> ServiceResponse<T> succeed(HttpStatus status, T data) {
    return new ServiceResponse<>(status.value(), data, null);
  }

  public static <T> ServiceResponse<T> succeed(HttpStatus status, T data, String message) {
    return new ServiceResponse<>(status.value(), data, message);
  }
}
