package com.labate.mentoringme.service.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.labate.mentoringme.dto.mapper.NotificationMapper;
import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.request.NotificationRequestDto;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.SubscriptionRequestDto;
import com.labate.mentoringme.dto.response.PageResponse;
import com.labate.mentoringme.dto.response.Paging;
import com.labate.mentoringme.model.FcmToken;
import com.labate.mentoringme.model.UnreadNotificationsCounter;
import com.labate.mentoringme.model.UserNotification;
import com.labate.mentoringme.repository.FcmTokenRepository;
import com.labate.mentoringme.repository.NotificationRepository;
import com.labate.mentoringme.repository.UnreadNotificationsCounterRepository;
import com.labate.mentoringme.repository.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

  @Value("${gcp.firebase-configuration-file}")
  private String firebaseConfig;

  private FirebaseApp firebaseApp;
  private final FcmTokenRepository fcmTokenRepository;
  private final NotificationRepository notificationRepository;
  private final UnreadNotificationsCounterRepository unreadNotificationsCounterRepository;
  private final UserNotificationRepository userNotificationRepository;

  @PostConstruct
  private void initialize() throws Exception {
    // TODO: Change this to read json file from classpath
    InputStream inputStream = new URL(firebaseConfig).openStream();

    try {
      FirebaseOptions options =
          FirebaseOptions.builder()
              .setCredentials(GoogleCredentials.fromStream(inputStream))
              .build();

      if (FirebaseApp.getApps().isEmpty()) {
        this.firebaseApp = FirebaseApp.initializeApp(options);
      } else {
        this.firebaseApp = FirebaseApp.getInstance();
      }
    } catch (IOException e) {
      log.error("Create FirebaseApp Error", e);
    }
  }

  public void subscribeToTopic(SubscriptionRequestDto subscriptionRequestDto)
      throws FirebaseMessagingException {
    try {
      FirebaseMessaging.getInstance(firebaseApp)
          .subscribeToTopic(
              subscriptionRequestDto.getTokens(), subscriptionRequestDto.getTopicName());
    } catch (FirebaseMessagingException e) {
      log.error("Firebase subscribe to topic fail", e);
      throw e;
    }
  }

  public void unsubscribeFromTopic(SubscriptionRequestDto subscriptionRequestDto)
      throws FirebaseMessagingException {
    try {
      FirebaseMessaging.getInstance(firebaseApp)
          .unsubscribeFromTopic(
              subscriptionRequestDto.getTokens(), subscriptionRequestDto.getTopicName());
    } catch (FirebaseMessagingException e) {
      log.error("Firebase unsubscribe from topic fail", e);
      throw e;
    }
  }

  public String sendPnsToDevice(NotificationRequestDto notificationRequestDto)
      throws FirebaseMessagingException {
    Message message = buildMessage(notificationRequestDto);

    String response = null;
    try {
      response = FirebaseMessaging.getInstance().send(message);
    } catch (FirebaseMessagingException e) {
      log.error("Fail to send firebase notification", e);
      throw e;
    }

    return response;
  }

  private Message buildMessage(NotificationRequestDto notificationRequestDto) {
    Message message =
        Message.builder()
            .setToken(notificationRequestDto.getTarget())
            .setNotification(
                Notification.builder()
                    .setBody(notificationRequestDto.getBody())
                    .setTitle(notificationRequestDto.getTitle())
                    .build())
            .putData("content", notificationRequestDto.getTitle())
            .putData("body", notificationRequestDto.getBody())
            .build();
    return message;
  }

  public String sendPnsToTopic(NotificationRequestDto notificationRequestDto)
      throws FirebaseMessagingException {
    Message message = buildMessage(notificationRequestDto);

    String response = null;
    try {
      FirebaseMessaging.getInstance().send(message);
    } catch (FirebaseMessagingException e) {
      log.error("Fail to send firebase notification", e);
      throw e;
    }

    return response;
  }

  @Transactional
  @Override
  public void registerToken(Long userId, String deviceToken) {
    var existFcmToken = fcmTokenRepository.findByUserIdAndToken(userId, deviceToken);
    if (existFcmToken == null) {
      var fcmToken = new FcmToken();
      fcmToken.setUserId(userId);
      fcmToken.setToken(deviceToken);
      fcmTokenRepository.save(fcmToken);

      // Check is new user
      var existsByUserId = unreadNotificationsCounterRepository.existsByUserId(userId);
      if (!existsByUserId) {
        var unreadNotificationCounter = new UnreadNotificationsCounter();
        unreadNotificationCounter.setUserId(userId);
        unreadNotificationCounter.setCounter(0);
        unreadNotificationsCounterRepository.save(unreadNotificationCounter);
      }

    } else if (existFcmToken.getIsDeleted()) {
      existFcmToken.setIsDeleted(false);
      fcmTokenRepository.save(existFcmToken);
    }
  }

  @Override
  public void unregisterToken(Long userId, String deviceToken) {
    var existFcmToken = fcmTokenRepository.findByUserIdAndToken(userId, deviceToken);
    if (existFcmToken != null) {
      existFcmToken.setIsDeleted(true);
      fcmTokenRepository.save(existFcmToken);
    }
  }

  @Override
  public int countUnreadNotifications(Long userId) {
    var counter = unreadNotificationsCounterRepository.countByUserId(userId);
    return counter == null ? 0 : counter;
  }

  @Transactional
  @Override
  public void markReadNotification(Long userId, String notificationId) {
    var userNotification =
        userNotificationRepository.findByUserIdAndNotificationId(userId, notificationId);
    if (userNotification != null) {
      userNotification.setIsRead(true);
      userNotificationRepository.save(userNotification);

      var unreadNotificationCounter = unreadNotificationsCounterRepository.findByUserId(userId);
      unreadNotificationCounter.decrementCounter();
      unreadNotificationsCounterRepository.save(unreadNotificationCounter);
    }
  }

  @Override
  public PageResponse getAllNotifications(Long userId, PageCriteria pageCriteria) {
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    var userNotificationPage = userNotificationRepository.findByUserId(userId, pageable);
    var notificationIds =
        userNotificationPage.stream()
            .map(UserNotification::getNotificationId)
            .collect(Collectors.toList());
    var notifications = notificationRepository.findAllById(notificationIds);
    var unreadNotificationCounter = countUnreadNotifications(userId);
    var notificationsResponse =
        NotificationMapper.toNotificationResponse(
            userNotificationPage.getContent(), notifications, unreadNotificationCounter);
    var paging =
        Paging.builder()
            .limit(pageCriteria.getLimit())
            .page(pageCriteria.getPage())
            .total(userNotificationPage.getTotalElements())
            .build();
    return new PageResponse(notificationsResponse, paging);
  }

  @Transactional
  @Override
  public void markAllReadNotifications(Long userId) {
    var userNotifications = userNotificationRepository.findByUserId(userId);
    if (!userNotifications.isEmpty()) {
      userNotifications.forEach(userNotification -> userNotification.setIsRead(true));
      userNotificationRepository.saveAll(userNotifications);

      var unreadNotificationCounter = unreadNotificationsCounterRepository.findByUserId(userId);
      unreadNotificationCounter.setCounter(0);
      unreadNotificationsCounterRepository.save(unreadNotificationCounter);
    }
  }
}
