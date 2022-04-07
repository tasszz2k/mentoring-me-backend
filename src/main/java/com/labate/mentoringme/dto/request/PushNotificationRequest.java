package com.labate.mentoringme.dto.request;

import lombok.Data;

@Data
public class PushNotificationRequest {
  private String topic;
  private String title;
  private String body;
}
