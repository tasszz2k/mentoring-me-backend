package com.labate.mentoringme.repository;

import com.labate.mentoringme.model.Event;
import com.labate.mentoringme.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByShiftId(Long shiftId);

    @Transactional
    @Modifying
    void deleteByShiftId(Long shiftId);

    List<Event> findAllByStartTimeAfter(Date startTime);

    @Query("SELECT e.id FROM Event e WHERE e.shiftId = :shiftId")
    List<Long> findIdsByShiftId(Long shiftId);
}
