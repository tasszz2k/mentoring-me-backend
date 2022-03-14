package com.labate.mentoringme.service.mentorshiprequest;

import com.labate.mentoringme.constant.UserRole;

public interface MentorshipRequestService {
    void request(Long mentorshipId, Long userId, UserRole userRole);
    void bookMentor(Long requestId, Long id);

}
