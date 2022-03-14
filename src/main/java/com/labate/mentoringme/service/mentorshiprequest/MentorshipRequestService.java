package com.labate.mentoringme.service.mentorshiprequest;

import com.labate.mentoringme.constant.UserRole;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateMentorshipRequestRq;
import com.labate.mentoringme.dto.request.GetMentorshipRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.model.MentorshipRequest;
import org.springframework.data.domain.Page;

public interface MentorshipRequestService {
  void request(Long mentorshipId, Long userId, UserRole userRole);

  void bookMentor(Long requestId, Long id);

  MentorshipRequest findById(Long id);

  Page<MentorshipRequest> findAllMentorshipRequestByConditions(
      PageCriteria pageCriteria, GetMentorshipRequest request);

  MentorshipRequest saveMentorshipRequest(MentorshipRequest entity);

  void updateMentorshipRequest(CreateMentorshipRequestRq request, LocalUser localUser);
}
