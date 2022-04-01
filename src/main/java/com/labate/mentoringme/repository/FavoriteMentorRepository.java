package com.labate.mentoringme.repository;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.labate.mentoringme.dto.projection.UserProjection;
import com.labate.mentoringme.model.FavoriteMentor;

public interface FavoriteMentorRepository extends JpaRepository<FavoriteMentor, Long> {
  @Query(
      value = " SELECT DISTINCT(B.id), B.full_name AS fullName, B.email, B.phone_number as phoneNumber, B.image_url AS imageUrl, A.created "
          + " FROM favorite_mentors A join users B ON A.mentor_id = B.id WHERE B.enabled = 1 AND A.is_deleted = 0 AND A.student_id = :userId",
      countQuery = " SELECT DISTINCT(B.id), B.full_name AS fullName, B.email, B.phone_number as phoneNumber, B.image_url AS imageUrl, A.created "
          + " FROM favorite_mentors A join users B ON A.mentor_id = B.id WHERE B.enabled = 1 AND A.is_deleted = 0 AND A.student_id = :userId",
      nativeQuery = true)
  Page<UserProjection> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

  @Transactional
  @Modifying
  @Query("UPDATE FavoriteMentor m SET m.isDeleted=1 WHERE m.studentId = :studentId AND m.mentorId = :mentorId")
  void deleteFavoriteMentor(@Param("studentId") Long userId, @Param("mentorId") Long mentorId);

  List<FavoriteMentor> findAllByStudentId(@Param("studentId") Long studentId);

  List<FavoriteMentor> findAllByStudentIdAndMentorId(@Param("studentId") Long studentId,
      @Param("mentorId") Long mentorId);
}
