package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.exception.InvalidTokenException;
import com.labate.mentoringme.service.verification.AccountVerificationService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/verification")
public class VerificationController {
  private static final String REDIRECT_LOGIN = "redirect:/signin";

  private final MessageSource messageSource;
  private final AccountVerificationService accountVerificationService;

  @GetMapping("/email")
  public String verifyEmail(@RequestParam(required = false) String token) {
    if (!StringUtils.hasText(token)) {
      return messageSource.getMessage(
          "user.registration.verification.missing.token", null, LocaleContextHolder.getLocale());
    }

    boolean isSuccess;
    try {
      isSuccess = accountVerificationService.verifyUser(token);
    } catch (InvalidTokenException e) {
      return messageSource.getMessage(
          "user.registration.verification.invalid.token", null, LocaleContextHolder.getLocale());
    }

    if (!isSuccess) {
      return messageSource.getMessage(
          "user.registration.verification.email.fail.verified",
          null,
          LocaleContextHolder.getLocale());
    }
    // TODO: Change response details
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
  @PostMapping("/email")
  public ResponseEntity<?> resendVerificationEmail(@CurrentUser LocalUser localUser) {
    var isSuccess =
        accountVerificationService.sendRegistrationConfirmationEmail(localUser.getUser());
    if (!isSuccess) {
      return BaseResponseEntity.fail(
          false,
          messageSource.getMessage(
              "user.registration.verification.email.fail.verified",
              null,
              LocaleContextHolder.getLocale()));
    }
    return BaseResponseEntity.ok(true, "Verification email sent");
  }
}
