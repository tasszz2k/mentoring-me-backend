package com.labate.mentoringme.dto.request;

import com.labate.mentoringme.model.Notification;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
public class PushNotificationToUserRequest {
  private Set<Long> userIds;
  private String title;
  private String body;
  private Notification.ObjectType objectType;
  private Long objectId;
}
