package com.labate.mentoringme.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.labate.mentoringme.dto.projection.QuizResultCheckingProjection;
import com.labate.mentoringme.model.quiz.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

  @Query(
      value = "SELECT A.id AS questionId, B.id AS answerId FROM questions A join answers B ON A.id = B.question_id WHERE B.is_correct = 1 AND A.quiz_id = :quizId",
      nativeQuery = true)
  List<QuizResultCheckingProjection> getQuizResult(@Param("quizId") Long quizId);

  @Modifying
  @Query("DELETE FROM Question q WHERE q.quizId = :quizId")
  void deleteByQuizId(@Param("quizId") Long quizId);

  List<Question> getByQuizId(Long quizId);
}
