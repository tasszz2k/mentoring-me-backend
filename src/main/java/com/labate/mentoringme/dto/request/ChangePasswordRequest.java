package com.labate.mentoringme.dto.request;

import com.labate.mentoringme.validator.PasswordValueMatch;
import com.labate.mentoringme.validator.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@PasswordValueMatch.List({
  @PasswordValueMatch(
      field = "newPassword",
      fieldMatch = "confirmNewPassword",
      message = "Passwords do not match!")
})
@Data
public class ChangePasswordRequest {

  private String oldPassword;

  @ValidPassword private String newPassword;

  @NotEmpty private String confirmNewPassword;
}
