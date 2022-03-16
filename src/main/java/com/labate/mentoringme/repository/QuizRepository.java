package com.labate.mentoringme.repository;

import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.labate.mentoringme.dto.projection.QuizOverviewProjection;
import com.labate.mentoringme.dto.projection.QuizTakingHistoryProjection;
import com.labate.mentoringme.dto.request.quiz.FindQuizRequest;
import com.labate.mentoringme.model.quiz.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

  @Query(
      value = " SELECT DISTINCT(A.id), title, time, number_of_question AS numberOfQuestion, author, A.created, A.created_by AS authorId "
          + " FROM quizzes A join quizzes_categories B ON A.id = B.quiz_id WHERE A.is_deleted = 0 "
          + " AND (:#{#cond.categoryId} IS NULL OR B.category_id = :#{#cond.categoryId})"
          + " AND (:#{#cond.userId} IS NULL OR A.created_by = :#{#cond.userId})"
          + " AND (:#{#cond.title} IS NULL OR LOWER(A.title) LiKE LOWER(CONCAT('%', :#{#cond.title}, '%')))"
          + " AND (:#{#cond.author} IS NULL OR LOWER(A.author) LiKE LOWER(CONCAT('%', :#{#cond.author}, '%')))"
          + " AND (:#{#cond.minNumberOfQuestion} IS NULL OR number_of_question >= :#{#cond.minNumberOfQuestion})"
          + " AND (:#{#cond.maxNumberOfQuestion} IS NULL OR number_of_question <= :#{#cond.maxNumberOfQuestion})"
          + " AND (:#{#cond.minTime} IS NULL OR time >= :#{#cond.minTime})"
          + " AND (:#{#cond.maxTime} IS NULL OR time <= :#{#cond.maxTime})"
          + " ORDER BY A.created desc",
      countQuery = "SELECT count(distinct(A.id)) "
          + " FROM quizzes A join quizzes_categories B ON A.id = B.quiz_id WHERE A.is_deleted = 0 "
          + " AND (:#{#cond.categoryId} IS NULL OR B.category_id = :#{#cond.categoryId})"
          + " AND (:#{#cond.userId} IS NULL OR A.created_by = :#{#cond.userId}) "
          + " AND (:#{#cond.title} IS NULL OR LOWER(A.title) LiKE LOWER(CONCAT('%', :#{#cond.title}, '%')))"
          + " AND (:#{#cond.author} IS NULL OR LOWER(A.author) LiKE LOWER(CONCAT('%', :#{#cond.author}, '%')))"
          + " AND (:#{#cond.minNumberOfQuestion} IS NULL OR number_of_question >= :#{#cond.minNumberOfQuestion})"
          + " AND (:#{#cond.maxNumberOfQuestion} IS NULL OR number_of_question <= :#{#cond.maxNumberOfQuestion})"
          + " AND (:#{#cond.minTime} IS NULL OR time >= :#{#cond.minTime})"
          + " AND (:#{#cond.maxTime} IS NULL OR time <= :#{#cond.maxTime})",
      nativeQuery = true)
  Page<QuizOverviewProjection> findAllByConditions(@Param("cond") FindQuizRequest request,
      Pageable pageable);

  Page<Quiz> findAllByCreatedByAndIsDraft(Long createdBy, Boolean isDraft, Pageable pageable);

  @Transactional
  @Modifying
  @Query("UPDATE Quiz q SET q.isDraft = 0 WHERE q.id = :id")
  void publishQuiz(@Param("id") Long id);

  @Query(
      value = "SELECT A.title, A.number_of_question AS numberOfQuestion, B.score, B.number_of_false AS numberOfFalse, B.number_of_true AS numberOfTrue, B.created, author FROM quizzes A join quiz_results B on A.id = B.quiz_id WHERE B.user_id = :userId",
      countQuery = "SELECT COUNT(B.id) FROM quizzes A join quiz_results B on A.id = B.quiz_id WHERE B.user_id = :userId",
      nativeQuery = true)
  Page<QuizTakingHistoryProjection> getQuizTakingHistory(@Param("userId") Long userId,
      Pageable pageable);
}
