package com.labate.mentoringme.service.mentorshiprequest;

import com.labate.mentoringme.model.Shift;

import java.util.Set;

public interface ShiftService {
  Shift findById(Long id);

  Set<Shift> findAllByIds(Set<Long> ids);

  Set<Shift> saveAllShifts(Long mentorshipId, Long createdBy, Set<Shift> shifts);
}
