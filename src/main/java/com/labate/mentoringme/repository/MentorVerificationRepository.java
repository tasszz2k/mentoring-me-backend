package com.labate.mentoringme.repository;

import com.labate.mentoringme.dto.request.GetMentorVerificationsRequest;
import com.labate.mentoringme.model.MentorVerification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MentorVerificationRepository extends JpaRepository<MentorVerification, Long> {

  boolean existsByMentorId(Long mentorId);

  MentorVerification findByMentorId(Long mentorId);

  @Query(
      "SELECT mv FROM MentorVerification mv WHERE "
          + " (:#{#request.status} is null or mv.status = :#{#request.status}) "
          + "and (:#{#request.fromDate} is null or mv.createdDate >= :#{#request.fromDate}) "
          + "and (:#{#request.toDate} is null or mv.createdDate <= :#{#request.toDate}) ")
  Page<MentorVerification> findAllByConditions(
      GetMentorVerificationsRequest request, Pageable pageable);
}
