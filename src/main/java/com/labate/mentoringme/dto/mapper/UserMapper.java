package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.constant.Gender;
import com.labate.mentoringme.constant.SocialProvider;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.model.UserDetails;
import com.labate.mentoringme.dto.model.UserInfo;
import com.labate.mentoringme.dto.request.UpdateUserProfileRequest;
import com.labate.mentoringme.model.User;
import com.labate.mentoringme.service.address.AddressService;
import com.labate.mentoringme.service.category.CategoryService;
import com.labate.mentoringme.service.user.LocalUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

  private static AddressService addressService;
  private static CategoryService categoryService;
  private static LocalUserDetailService localUserDetailService;

  @Autowired
  public UserMapper(
      AddressService addressService,
      CategoryService categoryService,
      LocalUserDetailService localUserDetailService) {
    UserMapper.addressService = addressService;
    UserMapper.categoryService = categoryService;
    UserMapper.localUserDetailService = localUserDetailService;
  }

  public static SocialProvider toSocialProvider(String providerId) {
    for (SocialProvider socialProvider : SocialProvider.values()) {
      if (socialProvider.getProviderType().equals(providerId)) {
        return socialProvider;
      }
    }
    return SocialProvider.LOCAL;
  }

  public static UserInfo buildUserInfo(User user) {
    var localUser = localUserDetailService.createLocalUser(user);
    return buildUserInfo(localUser);
  }

  public static List<UserInfo> toUserInfos(List<User> users) {
    return users.stream().map(UserMapper::buildUserInfo).collect(Collectors.toList());
  }

  public static UserDetails buildUserDetails(User user) {
    var localUser = localUserDetailService.createLocalUser(user);
    return buildUserDetails(localUser);
  }

  public static UserInfo buildUserInfo(LocalUser localUser) {
    var roles =
        localUser.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    var user = localUser.getUser();
    var profile = user.getUserProfile();

    Date dob = profile.getDob();

    return UserInfo.builder()
        .id(user.getId())
        .fullName(user.getFullName())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .imageUrl(user.getImageUrl())
        .enabled(user.isEnabled())
        .roles(roles)
        .verifiedEmail(user.isVerifiedEmail())
        .verifiedPhoneNumber(user.isVerifiedPhoneNumber())
        .provider(user.getProvider())
        .status(user.getStatus())
        .gender(profile.getGender())
        .dob(dob)
        .build();
  }

  public static UserDetails buildUserDetails(LocalUser localUser) {
    var userInfo = buildUserInfo(localUser);
    var userDetails = new UserDetails(userInfo);
    var profile = localUser.getUser().getUserProfile();
    var categories = CategoryMapper.toDtos(profile.getCategories());
    var addressDto = AddressMapper.toDto(profile.getAddress());

    userDetails.setSchool(profile.getSchool());
    userDetails.setDetailAddress(profile.getDetailAddress());
    userDetails.setAddress(addressDto);
    userDetails.setRating(profile.getRating());
    userDetails.setBio(profile.getBio());
    userDetails.setIsOfflineStudy(profile.getIsOfflineStudy());
    userDetails.setIsOnlineStudy(profile.getIsOnlineStudy());
    userDetails.setCategories(categories);
    userDetails.setPrice(profile.getPrice());

    return userDetails;
  }

  public static User toEntity(LocalUser localUser, UpdateUserProfileRequest request) {
    var user = localUser.getUser();
    var profile = user.getUserProfile();
    var address = addressService.findById(request.getAddressId());
    var categories = categoryService.findByIds(request.getCategoryIds());
    Gender gender = request.getGender();

    user.setFullName(request.getFullName());
    user.setPhoneNumber(request.getPhoneNumber());
    // user.setImageUrl(request.getImageUrl());
    profile.setGender(gender);
    // profile.setGenderValue(gender != null ? gender.getValue() : null);
    profile.setDob(request.getDob());
    profile.setSchool(request.getSchool());
    profile.setDetailAddress(request.getDetailAddress());
    profile.setBio(request.getBio());
    profile.setIsOfflineStudy(request.getIsOfflineStudy());
    profile.setIsOnlineStudy(request.getIsOnlineStudy());
    profile.setAddress(address);
    profile.setCategories(new HashSet<>(categories));
    profile.setPrice(request.getPrice());

    return user;
  }
}
