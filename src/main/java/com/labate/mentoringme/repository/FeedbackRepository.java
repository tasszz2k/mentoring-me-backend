package com.labate.mentoringme.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.labate.mentoringme.dto.projection.FeedbackProjection;
import com.labate.mentoringme.model.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

  @Query(
      value = "SELECT rating, comment, A.created, B.full_name AS fullName, B.image_url AS imageUrl from feedback_users A JOIN users B"
          + " ON A.from_user_id = B.id" + " WHERE A.is_deleted = 0 AND to_user_id = :toUserId ",
      countQuery = "SELECT count(A.id) from feedback_users A JOIN users B"
          + " ON A.from_user_id = B.id" + " WHERE A.is_deleted = 0 AND to_user_id = :toUserId ",
      nativeQuery = true)
  Page<FeedbackProjection> findByToUserId(@Param("toUserId") Long toUserId, Pageable pageable);

  List<Feedback> findByToUserId(Long toUserId);

  Feedback findByToUserIdAndFromUserId(Long toUserId, Long fromUserId);
}
