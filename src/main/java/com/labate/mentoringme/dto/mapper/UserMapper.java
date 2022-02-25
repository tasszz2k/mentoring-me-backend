package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.constant.SocialProvider;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.model.UserDetails;
import com.labate.mentoringme.dto.model.UserInfo;
import com.labate.mentoringme.util.ObjectMapperUtils;
import org.springframework.security.core.GrantedAuthority;

import javax.transaction.Transactional;
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
    var profile = user.getUserProfile();

    return UserInfo.builder()
        .id(user.getId())
        .fullName(user.getFullName())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .imageUrl(user.getImageUrl())
        .roles(roles)
        .verifiedEmail(user.isVerifiedEmail())
        .verifiedPhoneNumber(user.isVerifiedPhoneNumber())
        .status(user.getStatus())
        .gender(profile.getGender())
        .dob(profile.getDob())
        .build();
  }

  public static UserDetails buildUserDetails(LocalUser localUser) {
    var userInfo = buildUserInfo(localUser);
    var userDetails = new UserDetails(userInfo);
    var profile = localUser.getUser().getUserProfile();
    var categories = CategoryMapper.toDtos(profile.getCategories());

    userDetails.setSchool(profile.getSchool());
    userDetails.setDetailAddress(profile.getDetailAddress());
    userDetails.setRating(profile.getRating());
    userDetails.setBio(profile.getBio());
    userDetails.setIsOfflineStudy(profile.getIsOfflineStudy());
    userDetails.setIsOnlineStudy(profile.getIsOnlineStudy());
    userDetails.setCategories(categories);

    return userDetails;
  }
}
