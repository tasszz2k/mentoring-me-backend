package com.labate.mentoringme.repository;

import com.labate.mentoringme.dto.request.GetMentorshipRequestRq;
import com.labate.mentoringme.model.Class;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClassRepository extends JpaRepository<Class, Long> {
  @Query(
      "select c from Class c where "
          + " (:#{#request.mentorId} is null or c.mentorId = :#{#request.mentorId}) "
          + "AND (COALESCE(:#{#request.categoryIds}, NULL) IS NULL OR c.category.id IN (:#{#request.categoryIds})) "
          + "AND (COALESCE(:#{#request.addressIds}, NULL) IS NULL OR c.address.id IN (:#{#request.addressIds})) "
          + "and (:#{#request.minPrice} is null or c.price >= :#{#request.minPrice}) "
          + "and (:#{#request.maxPrice} is null or c.price <= :#{#request.maxPrice}) "
          + "and (:#{#request.fromDate} is null or c.startDate >= :#{#request.fromDate}) "
          + "and (:#{#request.toDate} is null or c.startDate <= :#{#request.toDate}) "
          + "and (:#{#request.createdBy} is null or c.createdBy = :#{#request.createdBy})")
  Page<Class> findAllByConditions(GetMentorshipRequestRq request, Pageable pageable);
}
