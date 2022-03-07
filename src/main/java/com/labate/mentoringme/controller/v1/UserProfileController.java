package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.mapper.UserMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.UpdateUserProfileRequest;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.service.user.UserService;
import com.labate.mentoringme.service.userprofile.UserProfileService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user-profiles")
public class UserProfileController {

  private final UserService userService;
  private final UserProfileService userProfileService;

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
  @PutMapping("/{userId}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  public ResponseEntity<?> updateProfile(
      @PathVariable Long userId,
      @RequestBody @Valid UpdateUserProfileRequest request,
      @CurrentUser LocalUser localUser) {
    if (!userId.equals(localUser.getUser().getId())) {
      throw new AccessDeniedException("You can't update other user's profile");
    }

    var user = UserMapper.toEntity(localUser, request);

    userService.save(user);
    return BaseResponseEntity.ok(null, "User profile updated successfully!");
  }
}
