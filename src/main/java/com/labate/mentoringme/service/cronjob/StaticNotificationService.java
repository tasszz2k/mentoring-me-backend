package com.labate.mentoringme.service.cronjob;

import com.labate.mentoringme.dto.request.PushNotificationToUserRequest;
import com.labate.mentoringme.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StaticNotificationService {
  private static NotificationService notificationService;

  @Autowired
  public StaticNotificationService(NotificationService notificationService) {
    StaticNotificationService.notificationService = notificationService;
  }

  public static void sendMulticast(PushNotificationToUserRequest request) {
    try {
      notificationService.sendMulticast(request);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
