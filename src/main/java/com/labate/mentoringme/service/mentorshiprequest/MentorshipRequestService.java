package com.labate.mentoringme.service.mentorshiprequest;

import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateMentorshipRequestRq;
import com.labate.mentoringme.dto.request.GetMentorshipRequestRq;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.model.Class;
import org.springframework.data.domain.Page;

public interface MentorshipRequestService {
  Class findById(Long id);

  Page<Class> findAllClassesByConditions(PageCriteria pageCriteria, GetMentorshipRequestRq request);

  Class saveMentorshipRequest(Class entity);

  void checkPermissionToUpdate(Class entity, LocalUser localUser);

  void deleteMentorshipRequest(Long id);

  Class updateMentorshipRequest(CreateMentorshipRequestRq request, LocalUser localUser);

  void deleteMentorshipRequest(Long id, LocalUser localUser);

}
