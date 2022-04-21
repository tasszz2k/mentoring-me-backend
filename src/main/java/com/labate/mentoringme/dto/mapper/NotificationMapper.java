package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.dto.model.NotificationDto;
import com.labate.mentoringme.dto.response.GetNotificationsResponse;
import com.labate.mentoringme.model.Notification;
import com.labate.mentoringme.model.UserNotification;
import com.labate.mentoringme.util.ObjectMapperUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class NotificationMapper {

  public static GetNotificationsResponse toNotificationResponse(
      Collection<UserNotification> userNotifications,
      Collection<Notification> notifications,
      int unreadNotificationCounter) {
    if (userNotifications == null || notifications == null) {
      return null;
    }
    var notificationMap =
        notifications.stream().collect(Collectors.toMap(Notification::getId, n -> n));
    var notificationDtos =
        userNotifications.stream()
            .map(
                n -> {
                  var notification = notificationMap.get(n.getNotificationId());
                  if (notification == null) {
                    return null;
                  }
                  var dto = ObjectMapperUtils.map(notification, NotificationDto.class);
                  dto.setRead(n.getIsRead());
                  return dto;
                })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    return new GetNotificationsResponse(notificationDtos, unreadNotificationCounter);
  }
}
