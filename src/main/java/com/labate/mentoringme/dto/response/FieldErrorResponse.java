package com.labate.mentoringme.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class FieldErrorResponse {
  private String field;

  private String objectName;

  private String message;
}
