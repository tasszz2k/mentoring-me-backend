package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.mapper.UserMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.model.UserDetails;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.service.user.UserService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user-profiles")
public class UserProfileController {

  private final UserService userService;

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @GetMapping("/me")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  public ResponseEntity<?> getCurrentUserProfile(@CurrentUser LocalUser user) {
    return BaseResponseEntity.ok(UserMapper.buildUserDetails(user));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<?> findUserProfileById(@PathVariable Long userId) {
    return BaseResponseEntity.ok(
        UserMapper.buildUserDetails(userService.findLocalUserById(userId)));
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PutMapping("")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  public ResponseEntity<?> updateProfile(@CurrentUser LocalUser user, @RequestBody UserDetails userDetails) {
    return BaseResponseEntity.ok(UserMapper.buildUserDetails(user));
  }


}
