package com.labate.mentoringme.service.mentorshiprequest;

import com.labate.mentoringme.constant.UserRole;
import com.labate.mentoringme.exception.CanNotReEnrollException;
import com.labate.mentoringme.exception.ClassHasBegunException;
import com.labate.mentoringme.exception.MentorshipRequestNotFoundException;
import com.labate.mentoringme.model.MentorshipRequest;
import com.labate.mentoringme.model.Mentorship;
import com.labate.mentoringme.repository.MentorshipRequestRepository;
import com.labate.mentoringme.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Slf4j
@Service
public class MentorshipRequestServiceImpl implements MentorshipRequestService {
  private final MentorshipRequestRepository mentorshipRequestRepository;
  private final MentorshipService mentorshipService;
  private final RoleRepository roleRepository;

  @Override
  public void bookMentor(Long mentorshipId, Long studentId) {
    var classEntity = mentorshipService.findById(mentorshipId);
    if (classEntity == null) {
      throw new MentorshipRequestNotFoundException("id = " + mentorshipId);
    }
    boolean canEnroll = canRequest(classEntity);
    if (!canEnroll) {
      throw new ClassHasBegunException("id = " + mentorshipId);
    }

    var classEnrollment = mentorshipRequestRepository.findByMentorshipIdAndRequesterId(mentorshipId, studentId);
    if (classEnrollment == null) {
      var roleUser = roleRepository.findByName(UserRole.ROLE_USER.name());
      var newClassEnrollment =
          MentorshipRequest.builder()
              .mentorship(classEntity)
              .requesterId(studentId)
              .RequesterRole(roleUser)
              .build();
      mentorshipRequestRepository.save(newClassEnrollment);
    } else {
      throw new CanNotReEnrollException("id = " + mentorshipId);
    }
  }

  private boolean canRequest(Mentorship mentorship) {
    boolean canEnroll = true;
    var now = new Date();
    var startDate = mentorship.getStartDate();
    // Check if class status is started -> Return error ClassHasBegunException
    if (startDate != null && now.after(startDate)) {
      canEnroll = false;
    }
    // TODO: Check status
    return canEnroll;
  }

  @Override
  public void request(Long mentorshipId, Long userId, UserRole userRole) {
    log.info("Enrolling student {} to class {}", userId, mentorshipId);
  }
}
