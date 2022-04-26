package com.labate.mentoringme.constant;

public enum AvatarConstant {
  ADMIN("https://storage.googleapis.com/labate-image/images/admin.jpeg"),
  MODERATOR("https://storage.googleapis.com/labate-image/images/moderator.jpeg"),
  MENTOR_MALE("https://storage.googleapis.com/labate-image/images/mentor-male.jpeg"),
  MENTOR_FEMALE("https://storage.googleapis.com/labate-image/images/mentor-female.jpeg"),
  MENTOR_OTHER("https://storage.googleapis.com/labate-image/images/mentor-other.jpeg"),
  USER_MALE("https://storage.googleapis.com/labate-image/images/user-male.jpeg"),
  USER_FEMALE("https://storage.googleapis.com/labate-image/images/user-female.jpeg"),
  USER_OTHER("https://storage.googleapis.com/labate-image/images/user-other.jpeg"),
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
