package com.labate.mentoringme.repository;

import com.labate.mentoringme.dto.projection.QuizResultCheckingProjection;
import com.labate.mentoringme.model.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Quiz, Long> {

  @Query(
      value =
          "SELECT A.id AS questionId, B.id AS answerId FROM questions A join answers B ON A.id = B.question_id WHERE B.is_correct = 1 AND A.quiz_id = :quizId",
      nativeQuery = true)
  List<QuizResultCheckingProjection> getQuizResult(@Param("quizId") Long quizId);
}
