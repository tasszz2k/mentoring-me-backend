package com.labate.mentoringme.dto.model;

import com.labate.mentoringme.constant.Gender;
import lombok.Data;

import java.util.List;

@Data
public class BasicUserInfo {
  private final Long id;
  private final String email;
  private final String fullName;
  private final String phoneNumber;
  private final String imageUrl;
  private final Gender gender;
  private final List<String> roles;
}
