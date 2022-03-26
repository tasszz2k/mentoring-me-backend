package com.labate.mentoringme.dto.projection;

import com.labate.mentoringme.model.Event;

import java.util.Date;
import java.util.Set;

public interface TimetableProjection {
  Long getId();

  Long getUserId();

  String getName();

  Date getCreatedDate();

  Date getModifiedDate();

  Set<Event> getEvents();

  Boolean getIsDeleted();
}
