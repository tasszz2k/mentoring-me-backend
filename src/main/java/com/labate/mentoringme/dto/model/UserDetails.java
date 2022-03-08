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
  private AddressDto address;
  private String detailAddress;
  private Float rating;
  private Float price;
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
        userInfo.getEnabled(),
        userInfo.getRoles());
  }
}
