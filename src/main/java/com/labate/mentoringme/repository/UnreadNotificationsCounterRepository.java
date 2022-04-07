package com.labate.mentoringme.repository;

import com.labate.mentoringme.model.UnreadNotificationsCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
public interface UnreadNotificationsCounterRepository
    extends JpaRepository<UnreadNotificationsCounter, Long> {
  @Query("SELECT u.counter FROM UnreadNotificationsCounter u WHERE u.userId = :userId")
  Integer countByUserId(Long userId);

  UnreadNotificationsCounter findByUserId(Long userId);

  boolean existsByUserId(Long userId);

  @Transactional
  @Modifying
  @Query(
      value =
          "UPDATE unread_notifications_counters\n"
              + "SET unread_notifications_counter = unread_notifications_counter + :modifiedCounter\n"
              + "WHERE user_id IN (:userIds)",
      nativeQuery = true)
  void updateUnreadNotificationsCounter(Set<Long> userIds, int modifiedCounter);

  @Transactional
  @Modifying
  @Query(
          value =
                  "UPDATE unread_notifications_counters\n"
                          + "SET unread_notifications_counter = unread_notifications_counter + :modifiedCounter\n"
                          + "WHERE user_id = :userId",
          nativeQuery = true)
  void updateUnreadNotificationsCounter(Long userId, int modifiedCounter);
}
