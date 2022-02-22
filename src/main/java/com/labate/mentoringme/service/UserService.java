package com.labate.mentoringme.service;

import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.SignUpRequest;
import com.labate.mentoringme.exception.UserAlreadyExistAuthenticationException;
import com.labate.mentoringme.model.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Map;
import java.util.Optional;

public interface UserService {

  User registerNewUser(SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException;

  User findUserByEmail(String email);

  Optional<User> findUserById(Long id);

  LocalUser processUserRegistration(
      String registrationId,
      Map<String, Object> attributes,
      OidcIdToken idToken,
      OidcUserInfo userInfo);
}
