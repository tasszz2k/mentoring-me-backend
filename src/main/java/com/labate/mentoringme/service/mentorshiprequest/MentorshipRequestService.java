package com.labate.mentoringme.service.mentorshiprequest;

import com.labate.mentoringme.dto.request.GetMentorshipRequestRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.model.Class;
import org.springframework.data.domain.Page;

public interface MentorshipRequestService {
    Class findById(Long id);

    Page<Class> findAllClasses(PageCriteria pageCriteria, GetMentorshipRequestRequest request);
}
