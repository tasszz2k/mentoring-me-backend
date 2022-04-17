package com.labate.mentoringme.service.moderator;

import com.labate.mentoringme.constant.SocialProvider;
import com.labate.mentoringme.constant.UserRole;
import com.labate.mentoringme.dto.request.CreateModeratorRequest;
import com.labate.mentoringme.dto.response.BasicInforResponse;
import com.labate.mentoringme.exception.UserAlreadyExistAuthenticationException;
import com.labate.mentoringme.model.Role;
import com.labate.mentoringme.model.User;
import com.labate.mentoringme.model.UserProfile;
import com.labate.mentoringme.repository.RoleRepository;
import com.labate.mentoringme.service.user.UserService;
import com.labate.mentoringme.util.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class ModeratorServiceImpl implements ModeratorService {

  private final UserService userService;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public BasicInforResponse createModerator(CreateModeratorRequest request) {
    if (userService.existsByEmail(request.getEmail())) {
      throw new UserAlreadyExistAuthenticationException("email = " + request.getEmail());
    }
    User user = buildUser(request);
    user = userService.save(user);
    var basicInforResponse = ObjectMapperUtils.map(user, BasicInforResponse.class);
    // userService.flush();
    return basicInforResponse;
  }

  private User buildUser(CreateModeratorRequest request) {
    var user = new User();
    user.setFullName(request.getFullName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    // user roles
    final var roles = new HashSet<Role>();
    var role = UserRole.ROLE_MODERATOR;
    roles.add(roleRepository.findByName(role.name()));
    user.setRoles(roles);
    user.setProvider(SocialProvider.LOCAL.getProviderType());
    user.setEnabled(true);

    // user profile
    var userProfile = new UserProfile();
    userProfile.setIsDeleted(false);
    user.setUserProfile(userProfile);
    return user;
  }
}
