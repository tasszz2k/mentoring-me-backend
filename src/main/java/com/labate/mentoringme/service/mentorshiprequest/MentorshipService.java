package com.labate.mentoringme.service.mentorshiprequest;

import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateMentorshipRequest;
import com.labate.mentoringme.dto.request.GetMentorshipRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.model.Mentorship;
import com.labate.mentoringme.model.MentorshipRequest;
import org.springframework.data.domain.Page;

public interface MentorshipService {
  Mentorship findById(Long id);

  Page<Mentorship> findAllMentorshipByConditions(
      PageCriteria pageCriteria, GetMentorshipRequest request);

  Mentorship saveMentorship(Mentorship entity);

  void checkPermissionToUpdate(Mentorship entity, LocalUser localUser);

  void deleteMentorship(Long id);

  Mentorship updateMentorship(CreateMentorshipRequest request, LocalUser localUser);

  void deleteMentorship(Long id, LocalUser localUser);

  void bookMentor(MentorshipRequest mentorshipRequest, Long mentorId);
}
