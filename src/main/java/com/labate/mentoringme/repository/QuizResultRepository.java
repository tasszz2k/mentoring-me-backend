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
      value = "SELECT B.title, B.number_of_question AS numberOfQuestion, A.score, A.number_of_false AS numberOfFalse, A.number_of_true AS numberOfTrue, A.created, author FROM quiz_results A JOIN quizzes B on A.quiz_id = B.id WHERE A.user_id = :userId",
      countQuery = "SELECT COUNT(A.id) FROM quiz_results A JOIN quizzes B on A.quiz_id = B.id WHERE A.user_id = :userId",
      nativeQuery = true)
  Page<QuizTakingHistoryProjection> getQuizTakingHistory(@Param("userId") Long userId,
      Pageable pageable);
}
