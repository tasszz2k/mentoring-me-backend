package com.labate.mentoringme.service.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.*;
import com.labate.mentoringme.constant.MentorStatus;
import com.labate.mentoringme.dto.mapper.NotificationMapper;
import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.PushNotificationRequest;
import com.labate.mentoringme.dto.request.PushNotificationToUserRequest;
import com.labate.mentoringme.dto.request.SubscriptionRequestDto;
import com.labate.mentoringme.dto.response.PageResponse;
import com.labate.mentoringme.dto.response.Paging;
import com.labate.mentoringme.model.*;
import com.labate.mentoringme.repository.*;
import com.labate.mentoringme.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// @RequiredArgsConstructor
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
  private final UserService userService;

  @Autowired
  public NotificationServiceImpl(
      FcmTokenRepository fcmTokenRepository,
      NotificationRepository notificationRepository,
      UnreadNotificationsCounterRepository unreadNotificationsCounterRepository,
      UserNotificationRepository userNotificationRepository,
      @Lazy UserService userService) {
    this.fcmTokenRepository = fcmTokenRepository;
    this.notificationRepository = notificationRepository;
    this.unreadNotificationsCounterRepository = unreadNotificationsCounterRepository;
    this.userNotificationRepository = userNotificationRepository;
    this.userService = userService;
  }

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

  @Override
  public void subscribeToTopic(SubscriptionRequestDto subscriptionRequestDto)
      throws FirebaseMessagingException {
    // try {
    //   FirebaseMessaging.getInstance(firebaseApp)
    //       .subscribeToTopic(
    //           subscriptionRequestDto.getTokens(), subscriptionRequestDto.getTopic());
    // } catch (FirebaseMessagingException e) {
    //   log.error("Firebase subscribe to topic fail", e);
    //   throw e;
    // }
  }

  @Override
  public void unsubscribeFromTopic(SubscriptionRequestDto subscriptionRequestDto)
      throws FirebaseMessagingException {
    // try {
    //   FirebaseMessaging.getInstance(firebaseApp)
    //       .unsubscribeFromTopic(
    //           subscriptionRequestDto.getTokens(), subscriptionRequestDto.getTopicName());
    // } catch (FirebaseMessagingException e) {
    //   log.error("Firebase unsubscribe from topic fail", e);
    //   throw e;
    // }
  }

  @Async
  @Transactional
  @Override
  public void sendMulticast(PushNotificationToUserRequest request)
      throws FirebaseMessagingException {
    var userIds = request.getUserIds();
    var fcmTokens = fcmTokenRepository.findByUserIdIn(userIds);
    // [START send_multicast]
    // Create a list containing up to 500 registration tokens.
    // These registration tokens come from the client FCM SDKs.
    var registrationTokens =
        fcmTokens.stream()
            .filter(fcmToken -> !fcmToken.getIsDeleted())
            .map(FcmToken::getToken)
            .collect(Collectors.toList());

    MulticastMessage message = buildMulticastMessage(request, registrationTokens);
    BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
    // See the BatchResponse reference documentation for the contents of response.
    log.info(response.getSuccessCount() + " messages were sent successfully");
    // [END send_multicast]

    // Save to database
    saveNotifications(
        userIds,
        request.getTitle(),
        request.getBody(),
        request.getObjectType(),
        request.getObjectId());
  }

  private void saveNotifications(
      Set<Long> userIds,
      String title,
      String body,
      com.labate.mentoringme.model.Notification.ObjectType objectType,
      Long objectId) {
    var notification =
        com.labate.mentoringme.model.Notification.builder()
            .title(title)
            .body(body)
            .objectType(objectType)
            .objectId(objectId)
            .build();
    var savedNotification = notificationRepository.save(notification);

    var userNotifications =
        userIds.stream()
            .map(
                userId ->
                    UserNotification.builder()
                        .userId(userId)
                        .notificationId(savedNotification.getId())
                        .build())
            .collect(Collectors.toList());

    userNotificationRepository.saveAll(userNotifications);

    // Update unread notifications counter
    unreadNotificationsCounterRepository.updateUnreadNotificationsCounter(userIds, 1);
  }

  private MulticastMessage buildMulticastMessage(
      PushNotificationToUserRequest request, List<String> registrationTokens) {
    return MulticastMessage.builder()
        .setNotification(
            Notification.builder().setBody(request.getBody()).setTitle(request.getTitle()).build())
        .putData("content", request.getTitle())
        .putData("body", request.getBody())
        .addAllTokens(registrationTokens)
        .build();
  }

  private Message buildMessage(PushNotificationToUserRequest request) {
    return Message.builder()
        // .setToken(request.getUserId())
        .setNotification(
            Notification.builder().setBody(request.getBody()).setTitle(request.getTitle()).build())
        .putData("content", request.getTitle())
        .putData("body", request.getBody())
        .build();
  }

  private Message buildMessage(PushNotificationRequest pushNotificationRequest) {
    return Message.builder()
        .setToken(pushNotificationRequest.getTopic())
        .setNotification(
            Notification.builder()
                .setBody(pushNotificationRequest.getBody())
                .setTitle(pushNotificationRequest.getTitle())
                .build())
        .putData("content", pushNotificationRequest.getTitle())
        .putData("body", pushNotificationRequest.getBody())
        .build();
  }

  @Async
  @Override
  public String sendPnsToTopic(PushNotificationRequest pushNotificationRequest)
      throws FirebaseMessagingException {
    Message message = buildMessage(pushNotificationRequest);

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
  public void markReadNotification(Long userId, Long notificationId) {
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
    var notifications = notificationRepository.findAllByIdInOrderByCreatedDateDesc(notificationIds);
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

  @Override
  public void sendAll(PushNotificationRequest request) {}

  @Async
  @Override
  public void sendMentorVerificationNotification(Long mentorId, MentorStatus mentorStatus) {
    var request =
        PushNotificationToUserRequest.builder()
            .userIds(Collections.singleton(mentorId))
            .objectType(com.labate.mentoringme.model.Notification.ObjectType.MENTOR_VERIFICATION)
            .objectId(mentorId)
            .build();

    switch (mentorStatus) {
      case ACCEPTED:
        request.setTitle("Hồ sơ của bạn đã được duyệt thành công");
        request.setBody(
            "Bạn đã trở thành mentor chính thức của MentoringMe! Kết nối với các học sinh ngay!");
        break;
      case REJECTED:
        request.setTitle("Hồ sơ của bạn bị từ chối");
        request.setBody(
            "Rất tiếc, bạn chưa đáp ứng yêu cầu của đội ngũ chúng tôi. Hãy học hỏi thêm các kiến thức từ các gia sư chất lượng tại MentoringMe bạn nhé!");
        break;
      default:
        return;
    }

    try {
      sendMulticast(request);
    } catch (Exception e) {
      log.error("Error sending notification to mentor {}", mentorId, e);
    }
  }

  @Async
  @Override
  public void sendMentorshipRequestNotification(MentorshipRequest mentorshipRequest) {
    var request =
        PushNotificationToUserRequest.builder()
            .objectType(com.labate.mentoringme.model.Notification.ObjectType.MENTORSHIP_REQUEST)
            .objectId(mentorshipRequest.getId())
            .build();

    Long creatorId = mentorshipRequest.getMentorship().getCreatedBy();
    Long mentorId = mentorshipRequest.getApproverId();
    switch (mentorshipRequest.getStatus()) {
      case ON_GOING:
        var creatorName = userService.findBasicUserInfoByUserId(creatorId).getFullName();
        var categoryName = mentorshipRequest.getMentorship().getCategory().getName();
        request.setUserIds(Collections.singleton(mentorId));
        request.setTitle("Yêu cầu gia sư mới");
        request.setBody(
            String.format(
                "%s vừa gửi tới bạn yêu cầu được học môn %s. Phản hồi học sinh ngay nào!",
                creatorName, categoryName));
        break;
      case REJECTED:
        var mentorName = userService.findBasicUserInfoByUserId(mentorId).getFullName();
        request.setUserIds(Collections.singleton(creatorId));
        request.setTitle("Yêu cầu gia sư của bạn bị từ chối");
        request.setBody(
            String.format(
                "Rất tiếc, gia sư %s không tiếp nhận yêu cầu từ bạn. Hãy gửi yêu cầu đến gia sư khác, hoặc gửi yêu cầu mới phù hợp với hồ sơ của gia sư này nhé!",
                mentorName));
        break;
      case APPROVED:
        request.setUserIds(Collections.singleton(creatorId));
        request.setTitle("Yêu cầu gia sư của bạn đã được gia sư đồng ý");
        request.setBody(
            "Lịch học của bạn đã được cập nhật trong lịch trình. Hãy trao đổi thêm với gia sư của bạn nhé.");
        break;
      default:
        return;
    }

    try {
      sendMulticast(request);
    } catch (Exception e) {
      log.error("Error sending mentorship request notification.", e);
    }
  }

  @Async
  @Override
  public void sendFeedbackNotification(Feedback feedback) {
    var studentName = userService.findBasicUserInfoByUserId(feedback.getFromUserId()).getFullName();

    var request =
        PushNotificationToUserRequest.builder()
            .userIds(Collections.singleton(feedback.getToUserId()))
            .objectType(com.labate.mentoringme.model.Notification.ObjectType.FEEDBACK)
            .objectId(feedback.getId())
            .title(String.format("Đánh giá mới từ %s", studentName))
            .body(String.format("%s vừa đăng một nhận xét mới về bạn", studentName))
            .build();

    try {
      sendMulticast(request);
    } catch (Exception e) {
      log.error("Error sending feedback notification.", e);
    }
  }

  @Async
  @Override
  public void sendCommentNotification(Comment comment, Post post) {
    var commenterName = userService.findBasicUserInfoByUserId(comment.getCreatedBy()).getFullName();

    var request =
        PushNotificationToUserRequest.builder()
            .userIds(Collections.singleton(post.getCreatedBy()))
            .objectType(com.labate.mentoringme.model.Notification.ObjectType.POST)
            .objectId(post.getId())
            .title(String.format("%s vừa bình luận về bài đăng của bạn", commenterName))
            .body(comment.getContent())
            .build();

    try {
      sendMulticast(request);
    } catch (Exception e) {
      log.error("Error sending comment notification.", e);
    }
  }

  @Async
  @Override
  public void sendLikePostNotification(Post post, Long userId) {
    var username = userService.findBasicUserInfoByUserId(userId).getFullName();

    var request =
        PushNotificationToUserRequest.builder()
            .userIds(Collections.singleton(post.getCreatedBy()))
            .objectType(com.labate.mentoringme.model.Notification.ObjectType.POST)
            .objectId(post.getId())
            .title("Bạn có thông báo mới")
            .body(String.format("%s vừa quan tâm về bài đăng của bạn", username))
            .build();

    try {
      sendMulticast(request);
    } catch (Exception e) {
      log.error("Error sending like post notification.", e);
    }
  }
}
