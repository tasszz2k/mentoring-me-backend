package com.labate.mentoringme.service.mentorshiprequest;

import com.labate.mentoringme.constant.UserRole;
import com.labate.mentoringme.exception.CanNotReEnrollException;
import com.labate.mentoringme.exception.ClassHasBegunException;
import com.labate.mentoringme.exception.MentorshipRequestNotFoundException;
import com.labate.mentoringme.model.ClassEnrollment;
import com.labate.mentoringme.repository.ClassEnrollmentRepository;
import com.labate.mentoringme.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Slf4j
@Service
public class ClassEnrollmentServiceImpl implements ClassEnrollmentService {
  private final ClassEnrollmentRepository classEnrollmentRepository;
  private final MentorshipRequestService mentorshipRequestService;
  private final RoleRepository roleRepository;

  @Override
  public void bookMentor(Long classId, Long studentId) {
    var classEntity = mentorshipRequestService.findById(classId);
    if (classEntity == null) {
      throw new MentorshipRequestNotFoundException("id = " + classId);
    }
    boolean canEnroll = canEnroll(classEntity);
    if (!canEnroll) {
      throw new ClassHasBegunException("id = " + classId);
    }

    var classEnrollment = classEnrollmentRepository.findByClassIdAndUserId(classId, studentId);
    if (classEnrollment == null) {
      var roleUser = roleRepository.findByName(UserRole.ROLE_USER.name());
      var newClassEnrollment =
          ClassEnrollment.builder()
              .classEntity(classEntity)
              .userId(studentId)
              .userRole(roleUser)
              .build();
      classEnrollmentRepository.save(newClassEnrollment);
    } else {
      throw new CanNotReEnrollException("id = " + classId);
    }
  }

  private boolean canEnroll(com.labate.mentoringme.model.Class classEntity) {
    boolean canEnroll = true;
    var now = new Date();
    var startDate = classEntity.getStartDate();
    // Check if class status is started -> Return error ClassHasBegunException
    if (startDate != null && now.after(startDate)) {
      canEnroll = false;
    }
    // TODO: Check status
    return canEnroll;
  }

  @Override
  public void enroll(Long classId, Long userId, UserRole userRole) {
    log.info("Enrolling student {} to class {}", userId, classId);
  }
}
