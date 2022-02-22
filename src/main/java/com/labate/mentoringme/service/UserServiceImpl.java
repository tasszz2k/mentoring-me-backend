package com.labate.mentoringme.service;

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
import com.labate.mentoringme.repository.UserRepository;
import com.labate.mentoringme.security.oauth2.user.OAuth2UserInfo;
import com.labate.mentoringme.security.oauth2.user.OAuth2UserInfoFactory;
import com.labate.mentoringme.util.GeneralUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional(value = "transactionManager")
  public User registerNewUser(final SignUpRequest signUpRequest)
      throws UserAlreadyExistAuthenticationException {
    if (signUpRequest.getUserID() != null && userRepository.existsById(signUpRequest.getUserID())) {
      throw new UserAlreadyExistAuthenticationException(
          "User with User id " + signUpRequest.getUserID() + " already exist");
    } else if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      throw new UserAlreadyExistAuthenticationException(
          "User with email id " + signUpRequest.getEmail() + " already exist");
    }
    User user = buildUser(signUpRequest);
    Date now = Calendar.getInstance().getTime();
    user.setCreatedDate(now);
    user.setModifiedDate(now);
    user = userRepository.save(user);
    userRepository.flush();
    return user;
  }

  private User buildUser(final SignUpRequest formDTO) {
    User user = new User();
    user.setFullName(formDTO.getFullName());
    user.setEmail(formDTO.getEmail());
    user.setPassword(passwordEncoder.encode(formDTO.getPassword()));

    final HashSet<Role> roles = new HashSet<Role>();
    roles.add(roleRepository.findByName(Role.ROLE_USER));
    user.setRoles(roles);
    user.setProvider(formDTO.getSocialProvider().getProviderType());
    user.setEnabled(true);
    user.setProviderUserId(formDTO.getProviderUserId());

    user.setUserProfile(new UserProfile());

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
        .addPassword("IMPORTANCE...CHANGE IT !!!") // FIXME: change it to a random password
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
      throw new UserNotFoundException("User with id " + id + " not found");
    }
    return LocalUser.create(user, null, null, null);
  }
}
