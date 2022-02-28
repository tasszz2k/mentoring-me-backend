package com.labate.mentoringme.dto.request;

import com.labate.mentoringme.validator.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordRequest {
  @NotBlank String token;
  @NotBlank String email;
  @ValidPassword private String newPassword;
}
