package com.labate.mentoringme.controller;

import com.labate.mentoringme.dto.*;
import com.labate.mentoringme.exception.UserAlreadyExistAuthenticationException;
import com.labate.mentoringme.security.jwt.TokenProvider;
import com.labate.mentoringme.service.UserService;
import com.labate.mentoringme.util.GeneralUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired AuthenticationManager authenticationManager;

  @Autowired UserService userService;

  @Autowired TokenProvider tokenProvider;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = tokenProvider.createToken(authentication);
    LocalUser localUser = (LocalUser) authentication.getPrincipal();
    return ResponseEntity.ok(
        new JwtAuthenticationResponse(jwt, GeneralUtils.buildUserInfo(localUser)));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    try {
      userService.registerNewUser(signUpRequest);
    } catch (UserAlreadyExistAuthenticationException e) {
      log.error("Exception Occurred", e);
      return new ResponseEntity<>(
          new ApiResponse(false, "Email Address already in use!"), HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok().body(new ApiResponse(true, "User registered successfully"));
  }
}
