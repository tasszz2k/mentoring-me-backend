package com.labate.mentoringme.dto.request;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class VerifyTokenRequest {
  @NotBlank String token;
  @NotBlank String email;
}
