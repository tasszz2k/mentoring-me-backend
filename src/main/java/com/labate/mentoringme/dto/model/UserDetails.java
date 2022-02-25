package com.labate.mentoringme.dto.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class UserDetails extends UserInfo {
  private String school;
  private String detailAddress;
  // TODO: Detail address level (Ward, District, Province)
  private Float rating;
  private String bio;
  private Boolean isOnlineStudy;
  private Boolean isOfflineStudy;
  private List<CategoryDto> categories;

  public UserDetails(UserInfo userInfo) {
    super(
        userInfo.getId(),
        userInfo.getFullName(),
        userInfo.getEmail(),
        userInfo.getPhoneNumber(),
        userInfo.getImageUrl(),
        userInfo.getGender(),
        userInfo.getDob(),
        userInfo.getProvider(),
        userInfo.getStatus(),
        userInfo.getVerifiedEmail(),
        userInfo.getVerifiedPhoneNumber(),
        userInfo.getRoles());
  }
}
