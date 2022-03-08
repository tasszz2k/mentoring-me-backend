package com.labate.mentoringme.dto.model;

import com.labate.mentoringme.constant.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

/** Use @AllArgsConstructor and private final to mimic the behaviour of @Value */
@SuperBuilder
@Getter
@AllArgsConstructor
public class UserInfo {
  private final Long id;
  private final String fullName;
  private final String email;
  private final String phoneNumber;
  private final String imageUrl;
  private final Gender gender;
  private final Date dob;
  private final String provider;
  private final Integer status;
  private final Boolean verifiedEmail;
  private final Boolean verifiedPhoneNumber;
  private final Boolean enabled;

  private final List<String> roles;
}
