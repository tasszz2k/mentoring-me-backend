package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.constant.SocialProvider;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.model.UserDetails;
import com.labate.mentoringme.dto.model.UserInfo;
import com.labate.mentoringme.dto.request.UpdateUserProfileRequest;
import com.labate.mentoringme.model.User;
import com.labate.mentoringme.service.address.AddressService;
import com.labate.mentoringme.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

  private static AddressService addressService;
  private static CategoryService categoryService;

  @Autowired
  public UserMapper(AddressService addressService, CategoryService categoryService) {
    UserMapper.addressService = addressService;
    UserMapper.categoryService = categoryService;
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
        .provider(user.getProvider())
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
    var addressDto = AddressMapper.toDto(profile.getAddress());

    userDetails.setSchool(profile.getSchool());
    userDetails.setDetailAddress(profile.getDetailAddress());
    userDetails.setAddress(addressDto);
    userDetails.setRating(profile.getRating());
    userDetails.setBio(profile.getBio());
    userDetails.setIsOfflineStudy(profile.getIsOfflineStudy());
    userDetails.setIsOnlineStudy(profile.getIsOnlineStudy());
    userDetails.setCategories(categories);

    return userDetails;
  }

  public static User toEntity(LocalUser localUser, UpdateUserProfileRequest request) {
    var user = localUser.getUser();
    var profile = user.getUserProfile();
    var address = addressService.findById(request.getAddressId());
    var categories = categoryService.findByIds(request.getCategoryIds());

    user.setFullName(request.getFullName());
    user.setPhoneNumber(request.getPhoneNumber());
    user.setImageUrl(request.getImageUrl());
    profile.setGender(request.getGender());
    profile.setDob(request.getDob());
    profile.setSchool(request.getSchool());
    profile.setDetailAddress(request.getDetailAddress());
    profile.setBio(request.getBio());
    profile.setIsOfflineStudy(request.getIsOfflineStudy());
    profile.setIsOnlineStudy(request.getIsOnlineStudy());
    profile.setAddress(address);
    profile.setCategories(Set.copyOf(categories));

    return user;
  }
}
