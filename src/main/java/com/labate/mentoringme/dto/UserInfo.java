package com.labate.mentoringme.dto;

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
