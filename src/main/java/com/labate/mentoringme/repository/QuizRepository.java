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
import com.labate.mentoringme.dto.request.quiz.FindQuizRequest;
import com.labate.mentoringme.model.quiz.Quiz;
import com.labate.mentoringme.projection.QuizOverviewProjection;
import com.labate.mentoringme.projection.QuizTakingHistoryProjection;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

  @Query(
      value = " SELECT DISTINCT(A.id), title, time, number_of_question, B.category_id, A.created "
          + " FROM quizzes A join quizzes_categories B ON A.id = B.quiz_id WHERE A.is_deleted = 0 "
          + " AND (:#{#cond.categoryId} IS NULL OR B.category_id = :#{#cond.categoryId})"
          + " AND (:#{#cond.userId} IS NULL OR A.created_by = :#{#cond.userId})"
          + " ORDER BY A.created desc",
      countQuery = "SELECT count(distinct(A.id)) "
          + " FROM quizzes A join quizzes_categories B ON A.id = B.quiz_id WHERE A.is_deleted = 0 "
          + " AND (:#{#cond.categoryId} IS NULL OR B.category_id = :#{#cond.categoryId})"
          + "	AND (:#{#cond.userId} IS NULL OR A.created_by = :#{#cond.userId}) ",
      nativeQuery = true)
  Page<QuizOverviewProjection> findAllByConditions(@Param("cond") FindQuizRequest request,
      Pageable pageable);

  List<Quiz> findAllByCreatedByAndIsDraft(Long createdBy, Boolean isDraft);

  @Transactional
  @Modifying
  @Query("UPDATE Quiz q SET q.isDraft = 0 WHERE q.id = :id")
  void saveDraftQuiz(@Param("id") Long id);

  @Query(
      value = "SELECT A.title, A.number_of_question, B.score, B.created FROM quizzes A join quiz_results B on A.id = B.quiz_id WHERE B.user_id = :userId",
      nativeQuery = true)
  List<QuizTakingHistoryProjection> getQuizTakingHistory(@Param("userId") Long userId);
}
