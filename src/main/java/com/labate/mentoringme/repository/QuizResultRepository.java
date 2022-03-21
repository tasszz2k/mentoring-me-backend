package com.labate.mentoringme.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.labate.mentoringme.dto.projection.QuizTakingHistoryProjection;
import com.labate.mentoringme.model.quiz.QuizResult;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
  @Query(
      value = "SELECT A.title, A.number_of_question AS numberOfQuestion, B.score, B.number_of_false AS numberOfFalse, B.number_of_true AS numberOfTrue, B.created, author FROM quizzes A join quiz_results B on A.id = B.quiz_id WHERE B.user_id = :userId",
      countQuery = "SELECT COUNT(B.id) FROM quizzes A join quiz_results B on A.id = B.quiz_id WHERE B.user_id = :userId",
      nativeQuery = true)
  Page<QuizTakingHistoryProjection> getQuizTakingHistory(@Param("userId") Long userId,
      Pageable pageable);
}
