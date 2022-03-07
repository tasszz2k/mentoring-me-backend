package com.labate.mentoringme.service.user;

import com.labate.mentoringme.constant.SocialProvider;
import com.labate.mentoringme.dto.mapper.UserMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.SignUpRequest;
import com.labate.mentoringme.exception.OAuth2AuthenticationProcessingException;
import com.labate.mentoringme.exception.UserAlreadyExistAuthenticationException;
import com.labate.mentoringme.exception.UserNotFoundException;
import com.labate.mentoringme.model.Role;
import com.labate.mentoringme.model.User;
import com.labate.mentoringme.model.UserProfile;
import com.labate.mentoringme.repository.RoleRepository;
import com.labate.mentoringme.repository.UserProfileRepository;
import com.labate.mentoringme.repository.UserRepository;
import com.labate.mentoringme.security.oauth2.user.OAuth2UserInfo;
import com.labate.mentoringme.security.oauth2.user.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserProfileRepository userProfileRepository;

  @Value("${labate.secure.default-password}")
  private String defaultPassword;

  @Override
  @Transactional(value = "transactionManager")
  public User registerNewUser(final SignUpRequest signUpRequest)
      throws UserAlreadyExistAuthenticationException {
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      throw new UserAlreadyExistAuthenticationException("email = " + signUpRequest.getEmail());
    }
    User user = buildUser(signUpRequest);
    // Date now = Calendar.getInstance().getTime();
    // user.setCreatedDate(now);
    // user.setModifiedDate(now);
    user = userRepository.save(user);
    userRepository.flush();
    return user;
  }

  private User buildUser(final SignUpRequest formDto) {
    var user = new User();
    user.setFullName(formDto.getFullName());
    user.setEmail(formDto.getEmail());
    user.setPassword(passwordEncoder.encode(formDto.getPassword()));

    // user roles
    final var roles = new HashSet<Role>();
    var role = formDto.getRole();
    roles.add(roleRepository.findByName(role.name()));
    user.setRoles(roles);
    user.setProvider(formDto.getSocialProvider().getProviderType());
    user.setEnabled(true);
    user.setProviderUserId(formDto.getProviderUserId());

    // user profile
    var userProfile = new UserProfile();
    userProfile.setIsDeleted(false);
    user.setUserProfile(userProfile);

    return user;
  }

  @Override
  public User findUserByEmail(final String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  @Transactional
  public LocalUser processUserRegistration(
      String registrationId,
      Map<String, Object> attributes,
      OidcIdToken idToken,
      OidcUserInfo userInfo) {
    OAuth2UserInfo oAuth2UserInfo =
        OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);
    if (!StringUtils.hasText(oAuth2UserInfo.getName())) {
      throw new OAuth2AuthenticationProcessingException("Name not found from OAuth2 provider");
    } else if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
      throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
    }
    SignUpRequest userDetails = toUserRegistrationObject(registrationId, oAuth2UserInfo);
    User user = findUserByEmail(oAuth2UserInfo.getEmail());
    if (user != null) {
      if (!user.getProvider().equals(registrationId)
          && !user.getProvider().equals(SocialProvider.LOCAL.getProviderType())) {
        throw new OAuth2AuthenticationProcessingException(
            "Looks like you're signed up with "
                + user.getProvider()
                + " account. Please use your "
                + user.getProvider()
                + " account to login.");
      }
      user = updateExistingUser(user, oAuth2UserInfo);
    } else {
      user = registerNewUser(userDetails);
    }

    return LocalUser.create(user, attributes, idToken, userInfo);
  }

  @Transactional
  @Override
  public User save(User user) {
    var updatedUser = userRepository.save(user);
    userProfileRepository.save(user.getUserProfile());
    return updatedUser;
  }

  @Override
  public void updateUserEnableStatus(Long userId, boolean enable) throws UserNotFoundException {
    var user = findUserById(userId).orElseThrow(() -> new UserNotFoundException("id = " + userId));
    if (user.isEnabled() != enable) {
      user.setEnabled(enable);
      userRepository.save(user);
    }
  }

  private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
    existingUser.setFullName(oAuth2UserInfo.getName());
    existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
    return userRepository.save(existingUser);
  }

  private SignUpRequest toUserRegistrationObject(
      String registrationId, OAuth2UserInfo oAuth2UserInfo) {
    return SignUpRequest.getBuilder()
        .addProviderUserID(oAuth2UserInfo.getId())
        .addFullName(oAuth2UserInfo.getName())
        .addEmail(oAuth2UserInfo.getEmail())
        .addSocialProvider(UserMapper.toSocialProvider(registrationId))
        .addPassword(defaultPassword) // FIXME: change it to a random password
        .build();
  }

  @Override
  public Optional<User> findUserById(Long id) {
    return userRepository.findById(id);
  }

  @Override
  public LocalUser findLocalUserById(Long id) {
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
      throw new UserNotFoundException("id = " + id);
    }
    return LocalUser.create(user, null, null, null);
  }
}
