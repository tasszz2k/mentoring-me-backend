package com.labate.mentoringme.dto.response;

import com.labate.mentoringme.dto.model.UserInfo;
import lombok.Value;

@Value
public class JwtAuthenticationResponse {
  String accessToken;
  String authChatToken;
  UserInfo user;
}
