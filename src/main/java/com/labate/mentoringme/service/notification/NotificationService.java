package com.labate.mentoringme.service.notification;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.labate.mentoringme.constant.MentorStatus;
import com.labate.mentoringme.dto.request.PushNotificationRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.PushNotificationToUserRequest;
import com.labate.mentoringme.dto.request.SubscriptionRequestDto;
import com.labate.mentoringme.dto.response.PageResponse;

public interface NotificationService {
  void subscribeToTopic(SubscriptionRequestDto subscriptionRequestDto) throws FirebaseMessagingException;

  void unsubscribeFromTopic(SubscriptionRequestDto subscriptionRequestDto) throws FirebaseMessagingException;

  void sendMulticast(PushNotificationToUserRequest request) throws FirebaseMessagingException;

  String sendPnsToTopic(PushNotificationRequest pushNotificationRequest) throws FirebaseMessagingException;

  void registerToken(Long userId, String deviceToken);

  void unregisterToken(Long userId, String deviceToken);

  int countUnreadNotifications(Long userId);

  void markReadNotification(Long userId, Long notificationId);

  PageResponse getAllNotifications(Long userId, PageCriteria pageCriteria);

  void markAllReadNotifications(Long userId);

  void sendAll(PushNotificationRequest request);

  void sendMentorVerificationNotification(Long mentorId, MentorStatus mentorStatus);
}
