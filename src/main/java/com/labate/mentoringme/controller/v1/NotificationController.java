package com.labate.mentoringme.controller.v1;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.model.UnreadNotificationCounterDto;
import com.labate.mentoringme.dto.request.*;
import com.labate.mentoringme.dto.request.quiz.RegisterTokenRequest;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.service.notification.NotificationService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

  private final NotificationService notificationService;

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  @PostMapping("register-token")
  public ResponseEntity<?> registerToken(
      @Valid @RequestBody RegisterTokenRequest request, @CurrentUser LocalUser localUser) {
    notificationService.registerToken(localUser.getUserId(), request.getDeviceToken());
    return BaseResponseEntity.ok(null, "Register token successfully");
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  @PostMapping("unregister-token")
  public ResponseEntity<?> unregisterToken(
      @Valid @RequestBody RegisterTokenRequest request, @CurrentUser LocalUser localUser) {
    notificationService.unregisterToken(localUser.getUserId(), request.getDeviceToken());
    return BaseResponseEntity.ok(null, "Unregister token successfully");
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  @GetMapping("count-unread-notifications")
  public ResponseEntity<?> countUnreadNotifications(@CurrentUser LocalUser localUser) {
    var counter = notificationService.countUnreadNotifications(localUser.getUserId());
    return BaseResponseEntity.ok(new UnreadNotificationCounterDto(counter));
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  @PatchMapping("mark-read-notification")
  public ResponseEntity<?> markReadNotification(
      @Valid @RequestBody MarkReadNotificationRequest request, @CurrentUser LocalUser localUser) {
    notificationService.markReadNotification(localUser.getUserId(), request.getNotificationId());
    return BaseResponseEntity.ok(null, "Mark read notification successfully");
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  @PatchMapping("mark-all-read-notifications")
  public ResponseEntity<?> markAllReadNotifications(@CurrentUser LocalUser localUser) {
    notificationService.markAllReadNotifications(localUser.getUserId());
    return BaseResponseEntity.ok(null, "Mark all read notification successfully");
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  @GetMapping("")
  public ResponseEntity<?> getAllNotifications(
      PageCriteria pageCriteria, @CurrentUser LocalUser localUser) {
    var notifications =
        notificationService.getAllNotifications(localUser.getUserId(), pageCriteria);
    return BaseResponseEntity.ok(notifications);
  }

  @PostMapping("/multicast")
  public ResponseEntity<?> sendMulticast(@RequestBody PushNotificationToUserRequest request)
      throws FirebaseMessagingException {
    notificationService.sendMulticast(request);
    return BaseResponseEntity.ok(null, "Send multicast successfully");
  }


  // @PostMapping("/topic")
  // public String sendPnsToTopic(@RequestBody PushNotificationRequest pushNotificationRequest)
  //     throws FirebaseMessagingException {
  //   return notificationService.sendPnsToTopic(pushNotificationRequest);
  // }
  //
  // @PostMapping("/subscribe")
  // public void subscribeToTopic(@RequestBody SubscriptionRequestDto subscriptionRequestDto)
  //     throws FirebaseMessagingException {
  //   notificationService.subscribeToTopic(subscriptionRequestDto);
  // }
  //
  // @PostMapping("/unsubscribe")
  // public void unsubscribeFromTopic(SubscriptionRequestDto subscriptionRequestDto)
  //     throws FirebaseMessagingException {
  //   notificationService.unsubscribeFromTopic(subscriptionRequestDto);
  // }

}
