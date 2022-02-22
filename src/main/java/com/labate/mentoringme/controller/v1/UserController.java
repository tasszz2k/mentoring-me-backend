package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.util.GeneralUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

  @GetMapping("/me")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  public ResponseEntity<?> getCurrentUser(@CurrentUser LocalUser user) {
    return ResponseEntity.ok(GeneralUtils.buildUserInfo(user));
  }

  @GetMapping("/all")
  public ResponseEntity<?> getContent() {
    return ResponseEntity.ok("Public content goes here");
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> getUserContent() {
    return ResponseEntity.ok("User content goes here");
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getAdminContent() {
    return ResponseEntity.ok("Admin content goes here");
  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> getModeratorContent() {
    return ResponseEntity.ok("Moderator content goes here");
  }
}
