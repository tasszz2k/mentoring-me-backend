package com.labate.mentoringme.repository;

import com.labate.mentoringme.model.UserNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
  UserNotification findByUserIdAndNotificationId(Long userId, String notificationId);

  Page<UserNotification> findByUserId(Long userId, Pageable pageable);
}
