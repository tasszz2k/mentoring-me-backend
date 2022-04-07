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
import com.labate.mentoringme.model.quiz.FavoriteQuiz;

@Repository
public interface FavoriteQuizRepository extends JpaRepository<FavoriteQuiz, Long> {

  FavoriteQuiz findByUserIdAndQuizId(@Param("userId") Long userId, @Param("quizId") Long quizId);

  @Transactional
  @Modifying
  @Query("UPDATE FavoriteQuiz q SET q.isDeleted=1 WHERE q.userId = :userId AND q.quizId = :quizId")
  void deleteFavoriteQuiz(@Param("userId") Long userId, @Param("quizId") Long quizId);

  List<FavoriteQuiz> findAllByUserId(Long userId);

  Page<FavoriteQuiz> findAllByUserId(Long userId, Pageable pageable);
}
