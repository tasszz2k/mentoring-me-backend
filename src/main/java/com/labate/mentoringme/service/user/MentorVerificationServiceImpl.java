package com.labate.mentoringme.service.user;

import com.labate.mentoringme.constant.MentorStatus;
import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.GetMentorVerificationsRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.RegisterBecomeMentorRequest;
import com.labate.mentoringme.dto.request.VerifyMentorRequest;
import com.labate.mentoringme.exception.UserAlreadyExistAuthenticationException;
import com.labate.mentoringme.exception.UserNotFoundException;
import com.labate.mentoringme.model.MentorVerification;
import com.labate.mentoringme.repository.MentorVerificationRepository;
import com.labate.mentoringme.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class MentorVerificationServiceImpl implements MentorVerificationService {
  private final MentorVerificationRepository mentorVerificationRepository;
  private final UserService userService;

  private final NotificationService notificationService;

  @Autowired
  public MentorVerificationServiceImpl(
      MentorVerificationRepository mentorVerificationRepository,
      @Lazy UserService userService,
      NotificationService notificationService) {
    this.mentorVerificationRepository = mentorVerificationRepository;
    this.userService = userService;
    this.notificationService = notificationService;
  }

  @Override
  public void registerMentor(Long mentorId, String message) {
    if (mentorVerificationRepository.existsByMentorId(mentorId)) {
      throw new UserAlreadyExistAuthenticationException("mentorId = " + mentorId);
    }

    var mentorVerification =
        MentorVerification.builder()
            .mentorId(mentorId)
            .status(MentorVerification.Status.IN_PROGRESS)
            .message(message)
            .build();
    mentorVerificationRepository.save(mentorVerification);
  }

  @Transactional
  @Override
  public void verifyMentor(VerifyMentorRequest request, LocalUser moderator) {
    Long mentorId = request.getMentorId();
    var mentorVerification = mentorVerificationRepository.findByMentorId(mentorId);
    if (mentorVerification == null) {
      throw new UserNotFoundException("mentorId = " + mentorId);
    }
    MentorVerification.Status status = request.getStatus();
    mentorVerification.setStatus(status);
    mentorVerification.setModeratorId(moderator.getUser().getId());
    mentorVerification.setVerifiedDate(new Date());
    mentorVerificationRepository.save(mentorVerification);

    MentorStatus mentorStatus = MentorStatus.valueOf(status.name());
    userService.updateMentorStatus(mentorId, mentorStatus);

    notificationService.sendMentorVerificationNotification(mentorId, mentorStatus);
  }

  @Override
  public Page<MentorVerification> findAllMentorVerificationsByConditions(
      GetMentorVerificationsRequest request, PageCriteria pageCriteria) {
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);

    return mentorVerificationRepository.findAllByConditions(request, pageable);
  }

  @Transactional
  @Override
  public void registerBecomeMentor(Long userId, RegisterBecomeMentorRequest request) {
    registerMentor(userId, request.getMessage());
    userService.updateMentorStatus(userId, MentorStatus.IN_PROGRESS);
  }
}
