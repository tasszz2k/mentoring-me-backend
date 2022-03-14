package com.labate.mentoringme.repository;

import com.labate.mentoringme.model.MentorshipRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MentorshipRequestRepository extends JpaRepository<MentorshipRequest, Long> {
  @Query(
      "select ce from MentorshipRequest ce where ce.mentorship.id = :mentorshipId and ce.requesterId = :requesterId")
  MentorshipRequest findByMentorshipIdAndRequesterId(Long mentorshipId, Long requesterId);
}
