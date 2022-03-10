package com.labate.mentoringme.service.mentorshiprequest;

import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.request.GetMentorshipRequestRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.model.Class;
import com.labate.mentoringme.repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;

@RequiredArgsConstructor
@Slf4j
@Service
public class MentorshipRequestServiceImpl implements MentorshipRequestService {
  private final ClassRepository classRepository;

  @Override
  public Class findById(Long id) {
    if (id == null) {
      return null;
    }
    return classRepository.findById(id).orElse(null);
  }

  @Override
  public Page<Class> findAllClasses(PageCriteria pageCriteria, GetMentorshipRequestRequest request) {
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    return classRepository.findAllByConditions(request, pageable);

  }
}
