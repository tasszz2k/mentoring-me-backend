package com.labate.mentoringme.service.mentorshiprequest;

import com.labate.mentoringme.constant.UserRole;
import com.labate.mentoringme.dto.mapper.MentorshipRequestMapper;
import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateMentorshipRequestRq;
import com.labate.mentoringme.dto.request.GetMentorshipRequestRq;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.exception.MentorshipRequestNotFoundException;
import com.labate.mentoringme.model.Class;
import com.labate.mentoringme.model.Shift;
import com.labate.mentoringme.repository.ClassRepository;
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
public class MentorshipRequestServiceImpl implements MentorshipRequestService {
  private final ClassRepository classRepository;
  private final ShiftService shiftService;

  @Override
  public Class findById(Long id) {
    if (id == null) {
      return null;
    }
    return classRepository.findById(id).orElse(null);
  }

  @Override
  public Page<Class> findAllClassesByConditions(
      PageCriteria pageCriteria, GetMentorshipRequestRq request) {
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    return classRepository.findAllByConditions(request, pageable);
  }

  @Transactional
  @Override
  public Class saveMentorshipRequest(Class entity) {
    var shifts = entity.getShifts();
    entity.setShifts(null);
    var savedClass = classRepository.save(entity);

    Set<Shift> savedShifts = shiftService.saveAllShifts(savedClass.getId(), shifts);
    savedClass.setShifts(savedShifts);
    return savedClass;
  }

  public void checkPermissionToUpdate(Class entity, LocalUser localUser) {
    var userId = localUser.getUser().getId();
    var role = localUser.getUser().getRole();

    if (!userId.equals(entity.getCreatedBy())
        && !userId.equals(entity.getMentorId())
        && !UserRole.MANAGER_ROLES.contains(role)) {
      throw new AccessDeniedException("You are not allowed to update this mentorship entity");
    }
  }

  @Override
  public void deleteMentorshipRequest(Long id) {
    classRepository.deleteById(id);
  }

  @Transactional
  @Override
  public Class updateMentorshipRequest(CreateMentorshipRequestRq request, LocalUser localUser) {
    var id = request.getId();
    var oldMentorshipRequest = findById(id);
    if (oldMentorshipRequest == null) {
      throw new MentorshipRequestNotFoundException("id = " + id);
    }
    checkPermissionToUpdate(oldMentorshipRequest, localUser);
    var entity = MentorshipRequestMapper.toEntity(request);
    // TODO: change to fields can update instead of all fields except (id, createdBy,...)
    entity.setCreatedBy(oldMentorshipRequest.getCreatedBy());

    return saveMentorshipRequest(entity);
  }

  @Override
  public void deleteMentorshipRequest(Long id, LocalUser localUser) {

    var oldMentorshipRequest = findById(id);
    if (oldMentorshipRequest == null) {
      throw new MentorshipRequestNotFoundException("id = " + id);
    }
    checkPermissionToUpdate(oldMentorshipRequest, localUser);
    deleteMentorshipRequest(id);
  }

}
