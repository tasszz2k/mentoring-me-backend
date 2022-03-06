package com.labate.mentoringme.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.labate.mentoringme.model.quiz.Quiz;
import com.labate.mentoringme.projection.QuizResultCheckingProjection;

@Repository
public interface QuestionRepository extends JpaRepository<Quiz, Long> {

  @Query(
      value = "SELECT A.id AS questionId, B.id AS answerId FROM questions A join answers B ON A.id = B.question_id WHERE B.is_correct = 1 AND A.quiz_id = :quizId",
      nativeQuery = true)
  List<QuizResultCheckingProjection> getQuizResult(@Param("quizId") Long quizId);
}
