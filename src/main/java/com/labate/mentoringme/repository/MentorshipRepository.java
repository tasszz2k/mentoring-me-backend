package com.labate.mentoringme.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.labate.mentoringme.dto.request.GetMentorshipRequest;
import com.labate.mentoringme.model.Mentorship;

public interface MentorshipRepository extends JpaRepository<Mentorship, Long> {
  @Query("select c from Mentorship c where "
      + " (:#{#request.mentorId} is null or c.mentorId = :#{#request.mentorId}) "
      + "AND (COALESCE(:#{#request.categoryIds}, NULL) IS NULL OR c.category.id IN (:#{#request.categoryIds})) "
      + "and (:#{#request.status} is null or c.status = :#{#request.status}) "
      + "and (:#{#request.minPrice} is null or c.price >= :#{#request.minPrice}) "
      + "and (:#{#request.maxPrice} is null or c.price <= :#{#request.maxPrice}) "
      + "and (:#{#request.fromDate} is null or c.startDate >= :#{#request.fromDate}) "
      + "and (:#{#request.toDate} is null or c.startDate <= :#{#request.toDate}) "
      + "and (:#{#request.createdBy} is null or c.createdBy = :#{#request.createdBy})")
  Page<Mentorship> findAllByConditions(GetMentorshipRequest request, Pageable pageable);

  List<Mentorship> findByMentorIdAndCreatedBy(@Param("MentodId") Long mentorId,
      @Param("CreatedBy") Long createdBy);
}
