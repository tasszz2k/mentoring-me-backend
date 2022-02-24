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
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/verification")
public class VerificationController {
  private static final String REDIRECT_LOGIN = "redirect:/signin";

  private final MessageSource messageSource;
  private final AccountVerificationService accountVerificationService;

  @GetMapping("/email")
  public String verifyEmail(
      @RequestParam(required = false) String token,
      final Model model,
      RedirectAttributes redirectAttributes) {
    if (!StringUtils.hasText(token)) {
      // Attr.addFlashAttribute(
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
      // Attr.addFlashAttribute(
      //     "tokenError",
      //     messageSource.getMessage(
      //         "user.registration.verification.invalid.token",
      //         null,
      //         LocaleContextHolder.getLocale()));
      // return REDIRECT_LOGIN;
      return messageSource.getMessage(
          "user.registration.verification.invalid.token", null, LocaleContextHolder.getLocale());
    }

    // Attr.addFlashAttribute(
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
  @PostMapping("/email")
  public ResponseEntity<?> resendVerificationEmail(@CurrentUser LocalUser localUser) {
    accountVerificationService.sendRegistrationConfirmationEmail(localUser.getUser());
    return BaseResponseEntity.ok(true, "Verification email sent");
  }
}
