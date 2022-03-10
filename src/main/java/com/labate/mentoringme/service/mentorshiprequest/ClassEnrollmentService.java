package com.labate.mentoringme.service.mentorshiprequest;

import com.labate.mentoringme.constant.UserRole;

public interface ClassEnrollmentService {
    void enroll(Long classId, Long userId, UserRole userRole);
    void bookMentor(Long requestId, Long id);

}
