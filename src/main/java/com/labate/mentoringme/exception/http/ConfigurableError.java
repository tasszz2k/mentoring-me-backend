package com.labate.mentoringme.exception.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurableError implements ResponseError {
  private String name;
  private String message;
  private int status;
}
