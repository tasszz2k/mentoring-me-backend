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

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

  @Query(value = "SELECT DISTINCT quiz FROM Quiz quiz JOIN quiz.categories category"
      + " WHERE quiz.isDraft = 0"
      + " AND (COALESCE(:#{#cond.categoryIds}, NULL) IS NULL OR category.id IN (:#{#cond.categoryIds})) "
      + " AND (:#{#cond.userId} IS NULL OR quiz.createdBy = :#{#cond.userId})"
      + " AND (:#{#cond.keyword} IS NULL OR LOWER(quiz.title) LiKE LOWER(CONCAT('%', :#{#cond.keyword}, '%')) OR LOWER(quiz.author) LiKE LOWER(CONCAT('%', :#{#cond.keyword}, '%')))"
      + " AND (:#{#cond.minNumberOfQuestion} IS NULL OR quiz.numberOfQuestion >= :#{#cond.minNumberOfQuestion})"
      + " AND (:#{#cond.maxNumberOfQuestion} IS NULL OR quiz.numberOfQuestion <= :#{#cond.maxNumberOfQuestion})"
      + " AND (:#{#cond.minTime} IS NULL OR quiz.time >= :#{#cond.minTime})"
      + " AND (:#{#cond.maxTime} IS NULL OR quiz.time <= :#{#cond.maxTime})",
      countQuery = "SELECT COUNT (DISTINCT quiz) FROM Quiz quiz JOIN quiz.categories category"
          + " WHERE quiz.isDraft = 0"
          + " AND (COALESCE(:#{#cond.categoryIds}, NULL) IS NULL OR category.id IN (:#{#cond.categoryIds})) "
          + " AND (:#{#cond.userId} IS NULL OR quiz.createdBy = :#{#cond.userId})"
          + " AND (:#{#cond.keyword} IS NULL OR LOWER(quiz.title) LiKE LOWER(CONCAT('%', :#{#cond.keyword}, '%')) OR LOWER(quiz.author) LiKE LOWER(CONCAT('%', :#{#cond.keyword}, '%')))"
          + " AND (:#{#cond.minNumberOfQuestion} IS NULL OR quiz.numberOfQuestion >= :#{#cond.minNumberOfQuestion})"
          + " AND (:#{#cond.maxNumberOfQuestion} IS NULL OR quiz.numberOfQuestion <= :#{#cond.maxNumberOfQuestion})"
          + " AND (:#{#cond.minTime} IS NULL OR quiz.time >= :#{#cond.minTime})"
          + " AND (:#{#cond.maxTime} IS NULL OR quiz.time <= :#{#cond.maxTime})")
  Page<Quiz> findAllByConditions(@Param("cond") FindQuizRequest request, Pageable pageable);

  Page<Quiz> findAllByCreatedByAndIsDraft(Long createdBy, Boolean isDraft, Pageable pageable);

  @Transactional
  @Modifying
  @Query("UPDATE Quiz q SET q.isDraft = 0 WHERE q.id = :id")
  void publishQuiz(@Param("id") Long id);

  @Query(
      value = "SELECT DISTINCT quiz FROM Quiz quiz JOIN quiz.categories category"
          + " WHERE quiz.isDraft = 0 AND quiz.id IN :ids ",
      countQuery = "SELECT COUNT (DISTINCT quiz) FROM Quiz quiz JOIN quiz.categories category"
          + " WHERE quiz.isDraft = 0 AND quiz.id IN :ids ")
  List<Quiz> findAllByIds(@Param("ids") List<Long> ids);
}
