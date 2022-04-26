package com.labate.mentoringme.constant;

public enum AvatarConstant {
  ADMIN("https://storage.googleapis.com/labate-image/images/admin.jpg"),
  MODERATOR("https://storage.googleapis.com/labate-image/images/moderator.jpg"),
  MENTOR_MALE("https://storage.googleapis.com/labate-image/images/mentor-male.jpg"),
  MENTOR_FEMALE("https://storage.googleapis.com/labate-image/images/mentor-female.jpg"),
  MENTOR_OTHER("https://storage.googleapis.com/labate-image/images/mentor-other.jpg"),
  USER_MALE("https://storage.googleapis.com/labate-image/images/user-male.jpg"),
  USER_FEMALE("https://storage.googleapis.com/labate-image/images/user-female.jpg"),
  USER_OTHER("https://storage.googleapis.com/labate-image/images/user-other.jpg"),
  ;

  private final String url;

  AvatarConstant(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public static String getUrl(UserRole userRole, Gender gender) {
    if (userRole == null) {
      return null;
    }
    switch (userRole) {
      case ROLE_ADMIN:
        return ADMIN.getUrl();
      case ROLE_MODERATOR:
        return MODERATOR.getUrl();
      case ROLE_MENTOR:
        if (gender == null) {
          return MENTOR_OTHER.getUrl();
        }
        switch (gender) {
          case MALE:
            return MENTOR_MALE.getUrl();
          case FEMALE:
            return MENTOR_FEMALE.getUrl();
        }
      case ROLE_USER:
        if (gender == null) {
          return USER_OTHER.getUrl();
        }
        switch (gender) {
          case MALE:
            return USER_MALE.getUrl();
          case FEMALE:
            return USER_FEMALE.getUrl();
        }
      default:
        return null;
    }
  }
}