package com.labate.mentoringme.repository;

import com.labate.mentoringme.model.ClassEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClassEnrollmentRepository extends JpaRepository<ClassEnrollment, Long> {
  @Query(
      "select ce from ClassEnrollment ce where ce.classEntity.id = :classId and ce.requesterId = :requesterId")
  ClassEnrollment findByClassIdAndRequesterId(Long classId, Long requesterId);
}
