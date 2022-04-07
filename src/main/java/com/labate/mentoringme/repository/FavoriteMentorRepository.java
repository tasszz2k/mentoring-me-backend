package com.labate.mentoringme.repository;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.labate.mentoringme.model.FavoriteMentor;

@Repository
public interface FavoriteMentorRepository extends JpaRepository<FavoriteMentor, Long> {

  @Transactional
  @Modifying
  @Query("UPDATE FavoriteMentor m SET m.isDeleted=1 WHERE m.studentId = :studentId AND m.mentorId = :mentorId")
  void deleteFavoriteMentor(@Param("studentId") Long userId, @Param("mentorId") Long mentorId);

  List<FavoriteMentor> findAllByStudentId(@Param("studentId") Long studentId);

  List<FavoriteMentor> findAllByStudentIdAndMentorId(@Param("studentId") Long studentId,
      @Param("mentorId") Long mentorId);

  Page<FavoriteMentor> findAllByStudentId(@Param("studentId") Long studentId, Pageable pageable);

  FavoriteMentor findByStudentIdAndMentorId(@Param("studentId") Long studentId,
      @Param("mentorId") Long mentorId);
}
