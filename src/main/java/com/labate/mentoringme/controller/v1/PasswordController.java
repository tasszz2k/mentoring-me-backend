package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.ChangePasswordRequest;
import com.labate.mentoringme.dto.request.ForgotPasswordRequest;
import com.labate.mentoringme.dto.request.ResetPasswordRequest;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.exception.InvalidTokenException;
import com.labate.mentoringme.service.password.PasswordService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/passwords")
public class PasswordController {

  private final PasswordService passwordService;

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

    var userId = localUser.getUserId();
    boolean isSuccess =
        passwordService.changePassword(userId, request.getOldPassword(), request.getNewPassword());

    return BaseResponseEntity.ok(isSuccess, "Password changed successfully");
  }

  @PostMapping("/reset")
  public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request)
      throws InvalidTokenException {
    passwordService.resetPassword(request);
    return BaseResponseEntity.ok(null, "Password reset successfully");
  }

  @PostMapping("/forgot")
  public ResponseEntity<?> forgotPassword(
      @Valid @RequestBody final ForgotPasswordRequest forgotPasswordRequest) {
    passwordService.forgottenPassword(forgotPasswordRequest);
    return BaseResponseEntity.ok(
        null,
        "If the email address entered matches your account, you will receive an email with a link to reset your password.");
  }
}
