package com.labate.mentoringme.dto.projection;

import java.util.List;

public interface BasicUserInfoProjection {

  Long getId();

  String getEmail();

  String getFullName();

  String getPhoneNumber();

  String getImageUrl();

  // Integer getGender();

  List<String> getRoles();

}
