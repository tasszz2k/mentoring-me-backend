package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.constant.SocialProvider;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.model.UserInfo;
import com.labate.mentoringme.model.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
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
    List<String> roles =
        localUser.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
    User user = localUser.getUser();
    return new UserInfo(
        user.getId().toString(), user.getFullName(), user.getEmail(), user.getImageUrl(), roles);
  }
}
