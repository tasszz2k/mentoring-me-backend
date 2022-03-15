package com.labate.mentoringme.repository;

import com.labate.mentoringme.model.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
  Timetable findByUserId(Long userId);
}
