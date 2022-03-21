package com.labate.mentoringme.controller;

import com.labate.mentoringme.dto.mapper.UserMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.LoginRequest;
import com.labate.mentoringme.dto.request.SignUpRequest;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.dto.response.JwtAuthenticationResponse;
import com.labate.mentoringme.exception.LoginFailBlockAccountException;
import com.labate.mentoringme.security.jwt.TokenProvider;
import com.labate.mentoringme.security.oauth2.AuthService;
import com.labate.mentoringme.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UserService userService;
  private final TokenProvider tokenProvider;
  private final AuthService authService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    return getResponseEntity(loginRequest.getEmail(), loginRequest.getPassword());
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    userService.registerNewUser(signUpRequest);
    return getResponseEntity(signUpRequest.getEmail(), signUpRequest.getPassword());
  }

  private ResponseEntity<?> getResponseEntity(String email, String password) {
    if (authService.isBruteForceAttack(email)) {
      throw new LoginFailBlockAccountException("Account is blocked");
    }

    Authentication authentication = authService.getAuthentication(email, password);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = tokenProvider.createToken(authentication);
    LocalUser localUser = (LocalUser) authentication.getPrincipal();
    return BaseResponseEntity.ok(
        new JwtAuthenticationResponse(jwt, UserMapper.buildUserInfo(localUser)));
  }
}
