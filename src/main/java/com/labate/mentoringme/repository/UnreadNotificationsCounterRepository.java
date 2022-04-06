package com.labate.mentoringme.repository;

import com.labate.mentoringme.model.UnreadNotificationsCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UnreadNotificationsCounterRepository
    extends JpaRepository<UnreadNotificationsCounter, Long> {
  @Query("SELECT u.counter FROM UnreadNotificationsCounter u WHERE u.userId = :userId")
  Integer countByUserId(Long userId);

  UnreadNotificationsCounter findByUserId(Long userId);

  boolean existsByUserId(Long userId);
}
