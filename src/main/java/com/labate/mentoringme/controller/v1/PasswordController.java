package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.ChangePasswordRequest;
import com.labate.mentoringme.dto.request.ResetPasswordRequest;
import com.labate.mentoringme.dto.response.ApiResponse;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.exception.InvalidTokenException;
import com.labate.mentoringme.exception.UserNotFoundException;
import com.labate.mentoringme.service.password.PasswordService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/password")
public class PasswordController {

  private final PasswordService passwordService;
  private final MessageSource messageSource;

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  @PostMapping("/change")
  public ResponseEntity<?> changePassword(
      @Valid @RequestBody ChangePasswordRequest request, @CurrentUser LocalUser localUser) {

    var userId = localUser.getUser().getId();
    boolean isSuccess =
        passwordService.changePassword(userId, request.getOldPassword(), request.getNewPassword());

    return BaseResponseEntity.ok(isSuccess);
  }

  @PostMapping("/reset")
  public ResponseEntity<?> resetPassword(
      @Valid @RequestBody ChangePasswordRequest request,
      @RequestParam(required = false) String token) {

    if (!StringUtils.hasText(token)) {
      return new ResponseEntity<>(
          ApiResponse.fail(
              false,
              messageSource.getMessage(
                  "user.registration.verification.invalid.token",
                  null,
                  LocaleContextHolder.getLocale())),
          HttpStatus.UNAUTHORIZED);
    }

    try {
      passwordService.resetPassword(token, request.getNewPassword());
    } catch (InvalidTokenException e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().body(ApiResponse.fail(false, e.getMessage()));
    }
    return BaseResponseEntity.ok(true, "Password reset successfully");
  }

  @PostMapping("/forgot")
  public ResponseEntity<?> forgotPassword(
      @Valid @RequestBody final ResetPasswordRequest resetPasswordRequest) {
    try {
      passwordService.forgottenPassword(resetPasswordRequest);
    } catch (UserNotFoundException e) {
      log.info("User not found: {}", resetPasswordRequest.getEmail());
      return ResponseEntity.badRequest().body(ApiResponse.fail(false, e.getMessage()));
    }
    // TODO: Change response details
    return ResponseEntity.ok(
        ApiResponse.success(
            true,
            messageSource.getMessage("user.forgotpwd.msg", null, LocaleContextHolder.getLocale())));
  }
}
