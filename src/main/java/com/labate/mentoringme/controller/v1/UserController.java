package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.mapper.UserMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  public ResponseEntity<?> getCurrentUser(@CurrentUser LocalUser user) {
    return BaseResponseEntity.ok(UserMapper.buildUserInfo(user));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<?> getUser(@PathVariable Long userId) {
    return BaseResponseEntity.ok(UserMapper.buildUserInfo(userService.findLocalUserById(userId)));
  }

  @GetMapping("")
  public ResponseEntity<?> getUsers() {
    return ResponseEntity.ok("Public content goes here");
  }
}
