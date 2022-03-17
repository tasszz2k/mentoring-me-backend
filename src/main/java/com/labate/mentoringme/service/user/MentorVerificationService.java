package com.labate.mentoringme.service.user;

import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.GetMentorVerificationsRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.VerifyMentorRequest;
import com.labate.mentoringme.model.MentorVerification;
import org.springframework.data.domain.Page;

public interface MentorVerificationService {

  void registerMentor(Long mentorId);

  void verifyMentor(VerifyMentorRequest request, LocalUser moderator);

  Page<MentorVerification> findAllMentorVerificationsByConditions(
          GetMentorVerificationsRequest request, PageCriteria pageCriteria);
}
