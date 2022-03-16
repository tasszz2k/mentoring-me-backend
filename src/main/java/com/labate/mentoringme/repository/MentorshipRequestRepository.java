package com.labate.mentoringme.repository;

import com.labate.mentoringme.dto.request.GetMentorshipRequestRq;
import com.labate.mentoringme.model.MentorshipRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MentorshipRequestRepository extends JpaRepository<MentorshipRequest, Long> {
  @Query(
      "select ce from MentorshipRequest ce where ce.mentorship.id = :mentorshipId and ce.assigneeId = :assigneeId")
  MentorshipRequest findByMentorshipIdAndAssigneeId(Long mentorshipId, Long assigneeId);

  @Query(
      "select mr from MentorshipRequest mr where "
          + " (:#{#request.status} is null or mr.status = :#{#request.status}) "
          + "and (:#{#request.createdBy} is null or mr.mentorship.createdBy = :#{#request.createdBy})"
          + "AND (:#{#request.mentorId} is null or mr.mentorship.mentorId = :#{#request.mentorId}) "
          + "AND (COALESCE(:#{#request.categoryIds}, NULL) IS NULL OR mr.mentorship.category.id IN (:#{#request.categoryIds})) "
          + "and (:#{#request.minPrice} is null or mr.mentorship.price >= :#{#request.minPrice}) "
          + "and (:#{#request.maxPrice} is null or mr.mentorship.price <= :#{#request.maxPrice}) "
          + "and (:#{#request.fromDate} is null or mr.mentorship.startDate >= :#{#request.fromDate}) "
          + "and (:#{#request.toDate} is null or mr.mentorship.startDate <= :#{#request.toDate}) ")
  Page<MentorshipRequest> findAllByConditions(GetMentorshipRequestRq request, Pageable pageable);
}
