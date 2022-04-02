package com.labate.mentoringme.controller.v1;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.constant.UserRole;
import com.labate.mentoringme.dto.mapper.UserMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.FindUsersRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.PartialUpdateUserProfileRequest;
import com.labate.mentoringme.dto.request.UpdateUserProfileRequest;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.dto.response.PageResponse;
import com.labate.mentoringme.dto.response.Paging;
import com.labate.mentoringme.service.user.UserService;
import com.labate.mentoringme.service.userprofile.UserProfileService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user-profiles")
public class UserProfileController {

  private final UserService userService;
  private final UserProfileService userProfileService;

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @GetMapping("/me")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  public ResponseEntity<?> getCurrentUserProfile(@CurrentUser LocalUser user) {
    return BaseResponseEntity.ok(UserMapper.buildUserDetails(user));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<?> findUserProfileById(@PathVariable Long userId,
      @CurrentUser LocalUser localUser) {
    var userProfile = userService.findUserProfileById(userId, localUser);
    return BaseResponseEntity.ok(userProfile);
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PutMapping("/{userId}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  public ResponseEntity<?> updateProfile(@PathVariable Long userId,
      @RequestBody @Valid UpdateUserProfileRequest request, @CurrentUser LocalUser localUser) {
    if (!userId.equals(localUser.getUserId())) {
      throw new AccessDeniedException("You can't update other user's profile");
    }

    var user = UserMapper.toEntity(localUser, request);

    userService.save(user);
    return BaseResponseEntity.ok(null, "User profile updated successfully!");
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PatchMapping("/{userId}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  public ResponseEntity<?> partialUpdateProfile(@PathVariable Long userId,
      @RequestBody @Valid PartialUpdateUserProfileRequest request,
      @CurrentUser LocalUser localUser) {

    request.setId(userId);
    userProfileService.partialUpdateProfile(localUser, request);
    return BaseResponseEntity.ok(null, "User profile updated successfully!");
  }

  @GetMapping("")
  public ResponseEntity<?> findAllUserProfiles(@Valid PageCriteria pageCriteria,
      @Valid FindUsersRequest request, @CurrentUser LocalUser localUser) {
    var users = userService.findAllUserProfile(pageCriteria, request, localUser);
    var paging = Paging.builder().limit(pageCriteria.getLimit()).page(pageCriteria.getPage())
        .total(users.getTotalElements()).build();
    var response = new PageResponse(users.getContent(), paging);
    return BaseResponseEntity.ok(response);
  }

  @GetMapping("/top-mentors")
  public ResponseEntity<?> findTop10MentorProfiles(@CurrentUser LocalUser localUser) {
    // TODO: build sort by ranking
    var sort = List.of("+createdDate");
    PageCriteria pageCriteria = PageCriteria.builder().limit(10).page(1).sort(sort).build();
    FindUsersRequest request = FindUsersRequest.builder().role(UserRole.ROLE_MENTOR).build();

    return findAllUserProfiles(pageCriteria, request, localUser);
  }
}
