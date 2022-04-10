package com.labate.mentoringme.repository;

import com.google.api.Page;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.model.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
  @Query(
      value =
          "SELECT notifications.*\n"
              + "FROM users_notifications\n"
              + "         LEFT JOIN notifications ON notifications.id = users_notifications.notification_id\n"
              + "WHERE users_notifications.user_id = 1",
      nativeQuery = true)
  List<Notification> findByUserId(Long userId, PageCriteria pageCriteria);

  List<Notification> findAllByIdInOrderByCreatedDateDesc(List<Long> notificationIds);
}
