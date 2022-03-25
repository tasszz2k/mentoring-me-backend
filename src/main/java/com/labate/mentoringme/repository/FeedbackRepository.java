package com.labate.mentoringme.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.labate.mentoringme.model.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
  Page<Feedback> findByToUserId(Long toUserId, Pageable pageable);

  List<Feedback> findByToUserId(Long toUserId);
}
