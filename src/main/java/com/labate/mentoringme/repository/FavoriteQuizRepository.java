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
import com.labate.mentoringme.model.quiz.FavoriteQuiz;

@Repository
public interface FavoriteQuizRepository extends JpaRepository<FavoriteQuiz, Long> {

  @Query(
      value = " SELECT DISTINCT(B.id), B.title, B.time, B.number_of_question AS numberOfQuestion, B.author, A.created, B.created_by AS authorId "
          + " FROM favorite_quizzes A join quizzes B ON A.quiz_id = B.id WHERE B.is_deleted = 0 AND A.is_deleted = 0 AND A.user_id = :userId",
      countQuery = " SELECT DISTINCT(B.id), B.title, B.time, B.number_of_question AS numberOfQuestion, B.author, A.created, B.created_by AS authorId "
          + " FROM favorite_quizzes A join quizzes B ON A.quiz_id = B.id WHERE B.is_deleted = 0 AND A.is_deleted = 0 AND A.user_id = :userId",
      nativeQuery = true)
  Page<QuizOverviewProjection> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

  FavoriteQuiz findByUserIdAndQuizId(Long userId, Long quizId);

  @Transactional
  @Modifying
  @Query("UPDATE FavoriteQuiz q SET q.isDeleted=1 WHERE q.userId = :userId AND q.quizId = :quizId")
  void deleteFavoriteQuiz(@Param("userId") Long userId, @Param("quizId") Long quizId);
}
