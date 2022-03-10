package com.labate.mentoringme.repository;

import com.labate.mentoringme.dto.request.GetMentorshipRequestRq;
import com.labate.mentoringme.model.Class;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClassRepository extends JpaRepository<Class, Long> {
  @Query("SELECT c FROM Class c")
  Page<Class> findAllByConditions(GetMentorshipRequestRq request, Pageable pageable);
}
