package com.labate.mentoringme.dto.projection;

import java.util.Date;

public interface FeedbackProjection {

  Long getToUserId();

  String getFullName();

  String getImageUrl();

  Integer getRating();

  String getComment();

  Date getCreated();
}
