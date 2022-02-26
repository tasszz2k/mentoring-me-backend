package com.labate.mentoringme.dto.request;

import com.labate.mentoringme.validator.ValidPassword;
import lombok.Data;

@Data
public class ResetPasswordRequest {

  @ValidPassword private String newPassword;
}
