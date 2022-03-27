package com.labate.mentoringme.dto.projection;

import java.util.Date;

public interface FeedbackProjection {
  String getFullName();

  String getImageUrl();

  Integer getRating();

  String getComment();

  Date getCreated();
}
