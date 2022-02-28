package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.mapper.UserMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.response.ApiResponse;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.exception.UserNotFoundException;
import com.labate.mentoringme.service.user.UserService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

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
  public ResponseEntity<?> getCurrentUser(@CurrentUser LocalUser user) {
    return BaseResponseEntity.ok(UserMapper.buildUserInfo(user));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<?> findUserById(@PathVariable Long userId) {
    return BaseResponseEntity.ok(UserMapper.buildUserInfo(userService.findLocalUserById(userId)));
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  @PatchMapping("/{userId}/enable")
  public ResponseEntity<?> enableUser(@PathVariable Long userId) {
    try {
      userService.updateUserEnableStatus(userId, true);
    } catch (UserNotFoundException e) {
      return ResponseEntity.badRequest().body(ApiResponse.fail(null, e.getMessage()));
    }
    return BaseResponseEntity.ok("User enabled");
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  @PatchMapping("/{userId}/disable")
  public ResponseEntity<?> disableUser(@PathVariable Long userId) {
    try {
      userService.updateUserEnableStatus(userId, false);
    } catch (UserNotFoundException e) {
      return ResponseEntity.badRequest().body(ApiResponse.fail(null, e.getMessage()));
    }
    return BaseResponseEntity.ok("User enabled");
  }
}
