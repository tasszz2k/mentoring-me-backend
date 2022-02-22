package com.labate.mentoringme.dto.model;

import lombok.Value;

import java.util.List;

@Value
public class UserInfo {
  String id;
  String fullName;
  String email;
  String imageUrl;
  List<String> roles;
}
