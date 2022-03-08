package com.labate.mentoringme.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import com.labate.mentoringme.validator.ValidPassword;
import lombok.Data;

@Data
public class CreateModeratorRequest {

  @NotEmpty
  private String fullName;

  @NotEmpty
  @Email
  private String email;

  private String phone;

  @ValidPassword
  private String password;

}
