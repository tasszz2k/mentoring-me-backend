package com.labate.mentoringme.service.mentorshiprequest;

import com.labate.mentoringme.model.StaticShift;
import com.labate.mentoringme.repository.StaticShiftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Service
public class StaticShiftServiceImpl implements StaticShiftService {
  private final StaticShiftRepository staticShiftRepository;

  @Override
  public StaticShift findById(Long id) {
    if (id == null) {
      return null;
    }
    return staticShiftRepository.findById(id).orElse(null);
  }

  @Override
  public Set<StaticShift> findAllByIds(Set<Long> ids) {
    return new HashSet<>(staticShiftRepository.findAllById(ids));
  }
}
