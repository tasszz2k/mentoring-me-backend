package com.labate.mentoringme.dto.model;

import lombok.Builder;
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
  String bio;
  String school;
  String detailAddress;
  // TODO: Detail address level (Ward, District, Province)
  Float rating;
  String provider;
  Integer status;
  Boolean verifiedEmail;
  Boolean verifiedPhoneNumber;

  List<String> roles;
}
