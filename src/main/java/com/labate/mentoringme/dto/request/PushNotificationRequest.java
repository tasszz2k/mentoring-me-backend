package com.labate.mentoringme.dto.request;

import com.labate.mentoringme.model.Notification;
import lombok.Data;

@Data
public class PushNotificationRequest {
  private String topic;
  private String title;
  private String body;
  private Notification.ObjectType objectType;
  private Long objectId;
}
