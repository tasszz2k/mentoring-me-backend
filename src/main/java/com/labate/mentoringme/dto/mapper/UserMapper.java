package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.constant.SocialProvider;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.model.UserInfo;
import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Collectors;

public class UserMapper {

  public static SocialProvider toSocialProvider(String providerId) {
    for (SocialProvider socialProvider : SocialProvider.values()) {
      if (socialProvider.getProviderType().equals(providerId)) {
        return socialProvider;
      }
    }
    return SocialProvider.LOCAL;
  }

  public static UserInfo buildUserInfo(LocalUser localUser) {
    var roles =
        localUser.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    var user = localUser.getUser();

    return UserInfo.builder()
        .id(user.getId())
        .fullName(user.getFullName())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .imageUrl(user.getImageUrl())
        .roles(roles)
        .gender(user.getUserProfile().getGender())
        .dob(user.getUserProfile().getDob())
        .rating(user.getUserProfile().getRating())
        .detailAddress(user.getUserProfile().getDetailAddress())
        .build();
  }
}