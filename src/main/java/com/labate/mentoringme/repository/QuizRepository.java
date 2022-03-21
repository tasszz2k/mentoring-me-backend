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
import com.labate.mentoringme.dto.request.quiz.FindQuizRequest;
import com.labate.mentoringme.model.quiz.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

  @Query(
      value = " SELECT DISTINCT(A.id), title, time, number_of_question AS numberOfQuestion, author, A.created, A.created_by AS authorId, C.user_id as userId"
          + " FROM quizzes A join quizzes_categories B ON A.id = B.quiz_id "
          + " LEFT JOIN favorite_quizzes C on A.id = C.quiz_id WHERE A.is_deleted = 0 AND A.is_draft = 0"
          + " AND (:#{#cond.categoryId} IS NULL OR B.category_id = :#{#cond.categoryId})"
          + " AND (:#{#cond.userId} IS NULL OR A.created_by = :#{#cond.userId})"
          + " AND (:#{#cond.title} IS NULL OR LOWER(A.title) LiKE LOWER(CONCAT('%', :#{#cond.title}, '%')))"
          + " AND (:#{#cond.author} IS NULL OR LOWER(A.author) LiKE LOWER(CONCAT('%', :#{#cond.author}, '%')))"
          + " AND (:#{#cond.minNumberOfQuestion} IS NULL OR number_of_question >= :#{#cond.minNumberOfQuestion})"
          + " AND (:#{#cond.maxNumberOfQuestion} IS NULL OR number_of_question <= :#{#cond.maxNumberOfQuestion})"
          + " AND (:#{#cond.minTime} IS NULL OR time >= :#{#cond.minTime})"
          + " AND (:#{#cond.maxTime} IS NULL OR time <= :#{#cond.maxTime})",
      countQuery = "SELECT count(distinct(A.id)) "
          + " FROM quizzes A join quizzes_categories B ON A.id = B.quiz_id "
          + " LEFT JOIN favorite_quizzes C on A.id = C.quiz_id WHERE A.is_deleted = 0 AND A.is_draft = 0"
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
}
