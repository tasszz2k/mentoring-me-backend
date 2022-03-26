package com.labate.mentoringme.repository;

import com.labate.mentoringme.dto.projection.TimetableProjection;
import com.labate.mentoringme.dto.request.GetTimetableRequest;
import com.labate.mentoringme.model.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
  Timetable findByUserId(Long userId);

  @Query(
      "SELECT t, e FROM Timetable t "
          + " LEFT JOIN FETCH t.events e"
          + " WHERE t.userId = :userId "
          + "AND (:#{#request.fromDate} IS NULL OR e.startTime >= :#{#request.fromDate}) "
          + "AND (:#{#request.toDate} IS NULL OR e.startTime <= :#{#request.toDate}) ")
  Timetable findByUserIdAndConditions(Long userId, GetTimetableRequest request);

  @Query(
      "SELECT t.id as id,"
          + "t.userId as userId,"
          + "t.name as name,"
          + "t.isDeleted as isDeleted,"
          + "t.createdDate as createdDate, "
          + "t.modifiedDate as modifiedDate "
          + "FROM Timetable t "
          + "WHERE t.userId = :userId "
          + "AND t.isDeleted = false ")
  TimetableProjection findTimetablePrjByUserId(Long userId);
}
