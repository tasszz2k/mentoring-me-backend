package com.labate.mentoringme.dto.request.quiz;

import com.labate.mentoringme.validator.ValidPassword;
import lombok.Data;

@Data
public class RegisterTokenRequest {

  private String deviceToken;
}
