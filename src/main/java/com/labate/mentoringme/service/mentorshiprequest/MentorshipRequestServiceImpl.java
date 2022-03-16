package com.labate.mentoringme.service.mentorshiprequest;

import com.labate.mentoringme.constant.UserRole;
import com.labate.mentoringme.dto.mapper.MentorshipRequestMapper;
import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateMentorshipRequestRq;
import com.labate.mentoringme.dto.request.GetMentorshipRequestRq;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.exception.CanNotReEnrollException;
import com.labate.mentoringme.exception.ClassHasBegunException;
import com.labate.mentoringme.exception.MentorshipNotFoundException;
import com.labate.mentoringme.exception.MentorshipRequestNotFoundException;
import com.labate.mentoringme.model.Mentorship;
import com.labate.mentoringme.model.MentorshipRequest;
import com.labate.mentoringme.repository.MentorshipRequestRepository;
import com.labate.mentoringme.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    var mentorship = mentorshipService.findById(mentorshipId);
    if (mentorship == null) {
      throw new MentorshipNotFoundException("id = " + mentorshipId);
    }
    boolean canEnroll = canRequest(mentorship);
    if (!canEnroll) {
      throw new ClassHasBegunException("id = " + mentorshipId);
    }

    var mentorshipRequest =
        mentorshipRequestRepository.findByMentorshipIdAndAssigneeId(mentorshipId, studentId);
    if (mentorshipRequest == null) {
      var roleUser = roleRepository.findByName(UserRole.ROLE_USER.name());
      var newMentorshipRequest =
          MentorshipRequest.builder()
              .mentorship(mentorship)
              .approverId(studentId)
              .assigneeRole(roleUser)
              .build();
      save(newMentorshipRequest);
    } else {
      throw new CanNotReEnrollException("id = " + mentorshipId);
    }
  }

  @Override
  public MentorshipRequest findById(Long id) {
    return mentorshipRequestRepository.findById(id).orElse(null);
  }

  @Override
  public Page<MentorshipRequest> findAllMentorshipRequestByConditions(
      PageCriteria pageCriteria, GetMentorshipRequestRq request) {
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    return mentorshipRequestRepository.findAllByConditions(request, pageable);
  }

  @Transactional
  @Override
  public MentorshipRequest save(MentorshipRequest entity) {
    var mentorship = mentorshipService.saveMentorship(entity.getMentorship());
    entity.setMentorship(mentorship);

    return mentorshipRequestRepository.save(entity);
  }

  @Transactional
  @Override
  public MentorshipRequest updateMentorshipRequest(
      CreateMentorshipRequestRq request, LocalUser localUser) {
    var id = request.getId();
    var oldMentorshipRequest = findById(id);
    if (oldMentorshipRequest == null) {
      throw new MentorshipRequestNotFoundException("id = " + id);
    }
    checkPermissionToUpdate(oldMentorshipRequest, localUser);
    var entity = MentorshipRequestMapper.toEntity(request);
    entity.getMentorship().setId(oldMentorshipRequest.getMentorship().getId());
    entity.getMentorship().setCreatedBy(oldMentorshipRequest.getMentorship().getCreatedBy());
    return save(entity);
  }

  @Transactional
  @Override
  public MentorshipRequest createNewMentorshipRequest(CreateMentorshipRequestRq request) {
    var entity = MentorshipRequestMapper.toEntity(request);
    entity.setId(null);
    return save(entity);
  }

  @Transactional
  @Override
  public void updateStatus(Long id, MentorshipRequest.Status status, LocalUser localUser) {
    var oldMentorshipRequest = findById(id);
    if (oldMentorshipRequest == null) {
      throw new MentorshipRequestNotFoundException("id = " + id);
    }

    checkPermissionToUpdateStatus(oldMentorshipRequest, localUser, status);
    oldMentorshipRequest.setStatus(status);
    var mentorshipRequest = mentorshipRequestRepository.save(oldMentorshipRequest);

    if (MentorshipRequest.Status.APPROVED.equals(status)
        && UserRole.ROLE_MENTOR.equals(mentorshipRequest.getAssigneeRole().getUserRole())) {
      // Update mentor into mentorship
      var mentorId = localUser.getUser().getId();
      mentorshipService.bookMentor(mentorshipRequest, mentorId);
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

  public void checkPermissionToUpdate(MentorshipRequest entity, LocalUser localUser) {
    var userId = localUser.getUser().getId();
    var role = localUser.getUser().getRole();

    if (!userId.equals(entity.getMentorship().getCreatedBy())
        && !userId.equals(entity.getMentorship().getMentorId())
        && !userId.equals(entity.getApproverId())
        && !UserRole.MANAGER_ROLES.contains(role)) {
      throw new AccessDeniedException("You are not allowed to update this mentorship request");
    }
  }

  public void checkPermissionToUpdateStatus(
      MentorshipRequest entity, LocalUser localUser, MentorshipRequest.Status status) {
    checkPermissionToUpdate(entity, localUser);

    if (UserRole.MANAGER_ROLES.contains(localUser.getUser().getRole())) {
      return;
    }
    if (MentorshipRequest.Status.COMPLETED_STATUSES.contains(entity.getStatus())) {
      throw new AccessDeniedException("You are not allowed to update this mentorship request");
    }
    // Status = APPROVED/REJECTED -> Check if user is assignee
    // Status = CANCELED -> Check if user is owner (createdBy)
    if (status == MentorshipRequest.Status.APPROVED
        || status == MentorshipRequest.Status.REJECTED) {
      if (!entity.getApproverId().equals(localUser.getUser().getId())) {
        throw new AccessDeniedException("You are not allowed to update this mentorship request");
      }
    } else if (status == MentorshipRequest.Status.CANCELED) {
      if (!entity.getMentorship().getCreatedBy().equals(localUser.getUser().getId())) {
        throw new AccessDeniedException("You are not allowed to update this mentorship request");
      }
    } else {
      throw new AccessDeniedException("You are not allowed to update this mentorship request");
    }
  }
}
