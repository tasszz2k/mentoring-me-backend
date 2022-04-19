package com.labate.mentoringme.service.cronjob;

import com.labate.mentoringme.dto.request.PushNotificationToUserRequest;

import java.util.Date;

public class TaskPushNotification extends TaskDefinition {

  private final PushNotificationToUserRequest request;

  public TaskPushNotification(PushNotificationToUserRequest request, Date startTime) {
    super(startTime);
    this.request = request;
  }

  @Override
  public void run() {
    StaticNotificationService.sendMulticast(request);
    System.out.println("executed task push notification: " + request);
  }
}
