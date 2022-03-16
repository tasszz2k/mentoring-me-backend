package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.mapper.UserMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.FindUsersRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.dto.response.PageResponse;
import com.labate.mentoringme.dto.response.Paging;
import com.labate.mentoringme.service.user.UserService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.stream.Collectors;

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
    userService.updateUserEnableStatus(userId, true);
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
    userService.updateUserEnableStatus(userId, false);
    return BaseResponseEntity.ok("User enabled");
  }

  @GetMapping("")
  public ResponseEntity<?> findAllUsers(
      @Valid PageCriteria pageCriteria, @Valid FindUsersRequest request) {
    var page = userService.findAllUsers(pageCriteria, request);
    var users = page.getContent();
    var paging =
        Paging.builder()
            .limit(pageCriteria.getLimit())
            .page(pageCriteria.getPage())
            .total(page.getTotalElements())
            .build();

    var userDtos = users.stream().map(UserMapper::buildUserInfo).collect(Collectors.toList());

    var response = new PageResponse(userDtos, paging);
    return BaseResponseEntity.ok(response);
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PostMapping("/avatar")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  public ResponseEntity<?> uploadAvatar(
      @RequestParam("image") @NotNull(message = "image is required") MultipartFile image,
      @CurrentUser LocalUser localUser)
      throws IOException {

    var url = userService.uploadAvatar(localUser, image);
    return BaseResponseEntity.ok(null, "Avatar uploaded");
  }
}
