package com.labate.mentoringme.util;

import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.constant.SocialProvider;
import com.labate.mentoringme.dto.model.UserInfo;
import com.labate.mentoringme.model.Role;
import com.labate.mentoringme.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GeneralUtils {

  public static List<SimpleGrantedAuthority> buildSimpleGrantedAuthorities(final Set<Role> roles) {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    for (Role role : roles) {
      authorities.add(new SimpleGrantedAuthority(role.getName()));
    }
    return authorities;
  }

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
