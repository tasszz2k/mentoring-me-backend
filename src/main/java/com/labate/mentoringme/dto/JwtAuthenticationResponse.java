package com.labate.mentoringme.dto;

import lombok.Value;

@Value
public class JwtAuthenticationResponse {
  String accessToken;
  UserInfo user;
}
