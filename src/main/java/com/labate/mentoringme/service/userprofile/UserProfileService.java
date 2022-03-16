package com.labate.mentoringme.service.userprofile;

import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.PartialUpdateUserProfileRequest;
import com.labate.mentoringme.model.UserProfile;

public interface UserProfileService {
  void save(UserProfile userProfile);

  void partialUpdateProfile(LocalUser localUser, PartialUpdateUserProfileRequest request);
}
