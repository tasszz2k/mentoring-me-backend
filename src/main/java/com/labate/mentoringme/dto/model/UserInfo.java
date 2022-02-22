package com.labate.mentoringme.dto.model;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Date;
import java.util.List;

@Builder
@Value
public class UserInfo {
  Long id;
  String fullName;
  String email;
  String phoneNumber;
  String imageUrl;
  Boolean gender;
  Date dob;
  Float rating;
  String detailAddress;
  String provider;
  Integer status;

  List<String> roles;
}
