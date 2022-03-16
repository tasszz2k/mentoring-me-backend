package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.dto.request.CreateModeratorRequest;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.service.moderator.ModeratorService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/moderators")
public class ModeratorController {

  private final ModeratorService moderatorService;

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN')")
  @PostMapping()
  public ResponseEntity<?> addModerator(@RequestBody CreateModeratorRequest request) {
    return BaseResponseEntity.ok(moderatorService.createModerator(request));
  }
}
