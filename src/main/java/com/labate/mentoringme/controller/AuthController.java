package com.labate.mentoringme.controller;

import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.mapper.UserMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.ChangePasswordRequest;
import com.labate.mentoringme.dto.request.LoginRequest;
import com.labate.mentoringme.dto.request.SignUpRequest;
import com.labate.mentoringme.dto.response.ApiResponse;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.dto.response.JwtAuthenticationResponse;
import com.labate.mentoringme.exception.InvalidTokenException;
import com.labate.mentoringme.exception.UserAlreadyExistAuthenticationException;
import com.labate.mentoringme.security.jwt.TokenProvider;
import com.labate.mentoringme.service.user.UserService;
import com.labate.mentoringme.service.verification.AccountVerificationService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private static final String REDIRECT_LOGIN = "redirect:/signin";

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final TokenProvider tokenProvider;
  private final MessageSource messageSource;
  private final AccountVerificationService accountVerificationService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = tokenProvider.createToken(authentication);
    LocalUser localUser = (LocalUser) authentication.getPrincipal();
    return BaseResponseEntity.ok(
        new JwtAuthenticationResponse(jwt, UserMapper.buildUserInfo(localUser)));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    try {
      userService.registerNewUser(signUpRequest);
    } catch (UserAlreadyExistAuthenticationException e) {
      log.error("Exception Occurred", e);
      return new ResponseEntity<>(
          ApiResponse.error(null, "Email Address already in use!"), HttpStatus.BAD_REQUEST);
    }
    return BaseResponseEntity.ok(true, "User registered successfully");
  }

  @GetMapping("/verification/email")
  public String verifyEmail(
      @RequestParam(required = false) String token,
      final Model model,
      RedirectAttributes redirAttr) {
    if (!StringUtils.hasText(token)) {
      // redirAttr.addFlashAttribute(
      //     "tokenError",
      //     messageSource.getMessage(
      //         "user.registration.verification.missing.token",
      //         null,
      //         LocaleContextHolder.getLocale()));
      // return REDIRECT_LOGIN;
      return messageSource.getMessage(
          "user.registration.verification.missing.token", null, LocaleContextHolder.getLocale());
    }
    try {
      var isSuccess = accountVerificationService.verifyUser(token);
    } catch (InvalidTokenException e) {
      // redirAttr.addFlashAttribute(
      //     "tokenError",
      //     messageSource.getMessage(
      //         "user.registration.verification.invalid.token",
      //         null,
      //         LocaleContextHolder.getLocale()));
      // return REDIRECT_LOGIN;
      return messageSource.getMessage(
          "user.registration.verification.invalid.token", null, LocaleContextHolder.getLocale());
    }

    // redirAttr.addFlashAttribute(
    //     "verifiedAccountMsg",
    //     messageSource.getMessage(
    //         "user.registration.verification.success", null, LocaleContextHolder.getLocale()));
    // return REDIRECT_LOGIN;
    return messageSource.getMessage(
        "user.registration.verification.success", null, LocaleContextHolder.getLocale());
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  @PostMapping("/verification/email")
  public ResponseEntity<?> resendVerificationEmail(@CurrentUser LocalUser localUser) {
    accountVerificationService.sendRegistrationConfirmationEmail(localUser.getUser());
    return BaseResponseEntity.ok(true, "Verification email sent");
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  @PostMapping("/pwd-change")
  public ResponseEntity<?> changePassword(
      @Valid @RequestBody ChangePasswordRequest request, @CurrentUser LocalUser localUser) {

    var userId = localUser.getUser().getId();
    boolean isSuccess =
        userService.changePassword(userId, request.getOldPassword(), request.getNewPassword());

    return BaseResponseEntity.ok(isSuccess);
  }
}
