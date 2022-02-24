package com.labate.mentoringme.dto.request;

import com.labate.mentoringme.validator.ValidPassword;
import lombok.Data;

@Data
public class ChangePasswordRequest {

  private String oldPassword;

  @ValidPassword private String newPassword;

  // @NotEmpty private String confirmNewPassword;
}
