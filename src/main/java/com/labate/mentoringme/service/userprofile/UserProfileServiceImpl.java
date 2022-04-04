package com.labate.mentoringme.service.userprofile;

import com.labate.mentoringme.constant.Gender;
import com.labate.mentoringme.constant.UserRole;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.PartialUpdateUserProfileRequest;
import com.labate.mentoringme.exception.UserNotFoundException;
import com.labate.mentoringme.model.UserProfile;
import com.labate.mentoringme.repository.UserProfileRepository;
import com.labate.mentoringme.service.address.AddressService;
import com.labate.mentoringme.service.category.CategoryService;
import com.labate.mentoringme.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class UserProfileServiceImpl implements UserProfileService {
  private final UserProfileRepository userProfileRepository;
  private final AddressService addressService;
  private final CategoryService categoryService;
  private final UserService userService;

  @Autowired
  public UserProfileServiceImpl(
      UserProfileRepository userProfileRepository,
      AddressService addressService,
      CategoryService categoryService,
      @Lazy UserService userService) {
    this.userProfileRepository = userProfileRepository;
    this.addressService = addressService;
    this.categoryService = categoryService;
    this.userService = userService;
  }

  @Override
  public void save(UserProfile userProfile) {
    userProfileRepository.save(userProfile);
  }

  @Transactional
  @Override
  public void partialUpdateProfile(LocalUser localUser, PartialUpdateUserProfileRequest request) {

    var user =
        userService
            .findUserById(request.getId())
            .orElseThrow(() -> new UserNotFoundException("id = " + request.getId()));

    var userProfile = user.getUserProfile();

    checkPermissionToUpdate(localUser.getUserId(), localUser);

    String fullName = request.getFullName();
    String phoneNumber = request.getPhoneNumber();
    String imageUrl = request.getImageUrl();
    Gender gender = request.getGender();
    Date dob = request.getDob();
    String school = request.getSchool();
    Long addressId = request.getAddressId();
    String detailAddress = request.getDetailAddress();
    String bio = request.getBio();
    Float price = request.getPrice();
    Boolean isOnlineStudy = request.getIsOnlineStudy();
    Boolean isOfflineStudy = request.getIsOfflineStudy();
    List<Long> categoryIds = request.getCategoryIds();

    if (fullName != null) user.setFullName(fullName);
    if (phoneNumber != null) user.setPhoneNumber(phoneNumber);
    if (imageUrl != null) user.setImageUrl(imageUrl);
    if (gender != null) userProfile.setGender(gender);
    if (dob != null) userProfile.setDob(dob);
    if (school != null) userProfile.setSchool(school);
    if (addressId != null) userProfile.setAddress(addressService.findById(addressId));
    if (detailAddress != null) userProfile.setDetailAddress(detailAddress);
    if (bio != null) userProfile.setBio(bio);
    if (price != null) userProfile.setPrice(price);
    if (isOnlineStudy != null) userProfile.setIsOnlineStudy(isOnlineStudy);
    if (isOfflineStudy != null) userProfile.setIsOfflineStudy(isOfflineStudy);
    if (categoryIds != null)
      userProfile.setCategories(new HashSet<>(categoryService.findByIds(categoryIds)));

    userService.save(user);
  }

  public void checkPermissionToUpdate(Long id, LocalUser localUser) {
    var userId = localUser.getUserId();
    var role = localUser.getUser().getRole();

    if (!userId.equals(id) && !UserRole.MANAGER_ROLES.contains(role)) {
      throw new AccessDeniedException("You are not allowed to update this profile");
    }
  }
}
