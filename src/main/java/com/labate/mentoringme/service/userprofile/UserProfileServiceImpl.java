package com.labate.mentoringme.service.userprofile;

import com.labate.mentoringme.model.UserProfile;
import com.labate.mentoringme.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserProfileServiceImpl implements UserProfileService {
  private final UserProfileRepository userProfileRepository;

  @Override
  public void save(UserProfile userProfile) {
    userProfileRepository.save(userProfile);
  }
}
