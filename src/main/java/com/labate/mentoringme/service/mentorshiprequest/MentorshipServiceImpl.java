package com.labate.mentoringme.service.mentorshiprequest;

import com.labate.mentoringme.constant.UserRole;
import com.labate.mentoringme.dto.mapper.MentorshipMapper;
import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateMentorshipRequest;
import com.labate.mentoringme.dto.request.GetMentorshipRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.exception.MentorshipNotFoundException;
import com.labate.mentoringme.model.Mentorship;
import com.labate.mentoringme.model.Shift;
import com.labate.mentoringme.repository.MentorshipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Service
public class MentorshipServiceImpl implements MentorshipService {
  private final MentorshipRepository mentorshipRepository;
  private final ShiftService shiftService;

  @Override
  public Mentorship findById(Long id) {
    if (id == null) {
      return null;
    }
    return mentorshipRepository.findById(id).orElse(null);
  }

  @Override
  public Page<Mentorship> findAllMentorshipByConditions(
      PageCriteria pageCriteria, GetMentorshipRequest request) {
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    return mentorshipRepository.findAllByConditions(request, pageable);
  }

  @Transactional
  @Override
  public Mentorship saveMentorship(Mentorship entity) {
    var shifts = entity.getShifts();
    entity.setShifts(null);
    var savedMentorship = mentorshipRepository.save(entity);

    Set<Shift> savedShifts = shiftService.saveAllShifts(savedMentorship.getId(), shifts);
    savedMentorship.setShifts(savedShifts);
    return savedMentorship;
  }

  public void checkPermissionToUpdate(Mentorship entity, LocalUser localUser) {
    var userId = localUser.getUser().getId();
    var role = localUser.getUser().getRole();

    if (!userId.equals(entity.getCreatedBy())
        && !userId.equals(entity.getMentorId())
        && !UserRole.MANAGER_ROLES.contains(role)) {
      throw new AccessDeniedException("You are not allowed to update this mentorship entity");
    }
  }

  @Override
  public void deleteMentorship(Long id) {
    mentorshipRepository.deleteById(id);
  }

  @Transactional
  @Override
  public Mentorship updateMentorship(CreateMentorshipRequest request, LocalUser localUser) {
    var id = request.getId();
    var oldMentorshipRequest = findById(id);
    if (oldMentorshipRequest == null) {
      throw new MentorshipNotFoundException("id = " + id);
    }
    checkPermissionToUpdate(oldMentorshipRequest, localUser);
    var entity = MentorshipMapper.toEntity(request);
    // TODO: change to fields can update instead of all fields except (id, createdBy,...)
    entity.setCreatedBy(oldMentorshipRequest.getCreatedBy());

    return saveMentorship(entity);
  }

  @Override
  public void deleteMentorship(Long id, LocalUser localUser) {

    var oldMentorshipRequest = findById(id);
    if (oldMentorshipRequest == null) {
      throw new MentorshipNotFoundException("id = " + id);
    }
    checkPermissionToUpdate(oldMentorshipRequest, localUser);
    deleteMentorship(id);
  }

}
