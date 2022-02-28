package com.labate.mentoringme.constant;

public enum UserRole {
  ROLE_ADMIN,
  ROLE_MODERATOR,
  ROLE_MENTOR,
  ROLE_USER;

  public static UserRole of(String role) {
    UserRole userRole = null;
    switch (role) {
      case "ROLE_ADMIN":
        userRole = ROLE_ADMIN;
        break;
      case "ROLE_MODERATOR":
        userRole = ROLE_MODERATOR;
        break;
      case "ROLE_MENTOR":
        userRole = ROLE_MENTOR;
        break;
      case "ROLE_USER":
        userRole = ROLE_USER;
        break;
      default:
        break;
    }
    return userRole;
  }
}
