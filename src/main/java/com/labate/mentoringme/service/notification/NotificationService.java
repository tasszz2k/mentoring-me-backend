package com.labate.mentoringme.service.notification;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.labate.mentoringme.dto.request.NotificationRequestDto;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.SubscriptionRequestDto;
import com.labate.mentoringme.dto.response.GetNotificationsResponse;
import com.labate.mentoringme.dto.response.PageResponse;

public interface NotificationService {
  void subscribeToTopic(SubscriptionRequestDto subscriptionRequestDto) throws FirebaseMessagingException;

  void unsubscribeFromTopic(SubscriptionRequestDto subscriptionRequestDto) throws FirebaseMessagingException;

  String sendPnsToDevice(NotificationRequestDto notificationRequestDto) throws FirebaseMessagingException;

  String sendPnsToTopic(NotificationRequestDto notificationRequestDto) throws FirebaseMessagingException;

  void registerToken(Long userId, String deviceToken);

  void unregisterToken(Long userId, String deviceToken);

  int countUnreadNotifications(Long userId);

  void markReadNotification(Long userId, String notificationId);

  PageResponse getAllNotifications(Long userId, PageCriteria pageCriteria);
}
