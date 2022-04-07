package com.labate.mentoringme.service.mentorshiprequest;

import com.labate.mentoringme.model.Shift;
import com.labate.mentoringme.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Service
public class ShiftServiceImpl implements ShiftService {
  private final ShiftRepository shiftRepository;

  @Override
  public Shift findById(Long id) {
    if (id == null) {
      return null;
    }
    return shiftRepository.findById(id).orElse(null);
  }

  @Override
  public Set<Shift> findAllByIds(Set<Long> ids) {
    return new HashSet<>(shiftRepository.findAllById(ids));
  }

  @Override
  public Set<Shift> saveAllShifts(Long mentorshipId, Long createdBy, Set<Shift> shifts) {
    shifts.forEach(
        shift -> {
          shift.setMentorshipId(mentorshipId);
          shift.setCreatedBy(createdBy);
        });
    return new HashSet<>(shiftRepository.saveAll(shifts));
  }
}
