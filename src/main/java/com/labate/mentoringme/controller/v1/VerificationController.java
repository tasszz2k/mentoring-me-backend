package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.mapper.MentorVerificationMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.*;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.dto.response.PageResponse;
import com.labate.mentoringme.dto.response.Paging;
import com.labate.mentoringme.exception.InvalidTokenException;
import com.labate.mentoringme.service.user.MentorVerificationService;
import com.labate.mentoringme.service.verification.AccountVerificationService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/verifications")
public class VerificationController {
  private static final String REDIRECT_LOGIN = "redirect:/signin";

  private final AccountVerificationService accountVerificationService;
  private final MentorVerificationService mentorVerificationService;

  @GetMapping("/email")
  public String verifyEmail(@RequestParam String token) throws InvalidTokenException {
    if (!StringUtils.hasText(token)) {
      throw new InvalidTokenException("Token is empty");
    }
    accountVerificationService.verifyUser(token);
    return "Thanks for the account verification. You can now login to your account.";
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
    return BaseResponseEntity.ok(null, "Verification email sent");
  }

  @PostMapping("/otp/verify")
  public ResponseEntity<?> verifyToken(@RequestBody @Valid VerifyTokenRequest request)
      throws InvalidTokenException {
    accountVerificationService.verifyToken(request);
    return BaseResponseEntity.ok(null, "Token is valid. You can now use for verification request.");
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  @PatchMapping("/mentors")
  public ResponseEntity<?> verifyMentor(
      @RequestBody @Valid VerifyMentorRequest request, @CurrentUser LocalUser localUser) {
    mentorVerificationService.verifyMentor(request, localUser);
    return BaseResponseEntity.ok(null, "Mentor status is updated to " + request.getStatus());
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  @GetMapping("/mentors")
  public ResponseEntity<?> findAllMentorVerificationsByConditions(
      @Valid PageCriteria pageCriteria, @Valid GetMentorVerificationsRequest request) {
    var page =
        mentorVerificationService.findAllMentorVerificationsByConditions(request, pageCriteria);
    var entities = page.getContent();

    var paging =
        Paging.builder()
            .limit(pageCriteria.getLimit())
            .page(pageCriteria.getPage())
            .total(page.getTotalElements())
            .build();
    var response = new PageResponse(MentorVerificationMapper.toDtos(entities), paging);
    return BaseResponseEntity.ok(response);
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PostMapping("/mentors/register")
  @PreAuthorize("hasAnyRole('USER')")
  public ResponseEntity<?> registerBecomeMentor(
      @RequestBody @Valid RegisterBecomeMentorRequest request, @CurrentUser LocalUser localUser) {
    mentorVerificationService.registerBecomeMentor(localUser.getUserId(), request);
    return BaseResponseEntity.ok(null, "Mentor request sent successfully");
  }
}
