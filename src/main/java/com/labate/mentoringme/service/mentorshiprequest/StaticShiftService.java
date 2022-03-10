package com.labate.mentoringme.service.mentorshiprequest;

import com.labate.mentoringme.model.StaticShift;

import java.util.Set;

public interface StaticShiftService {
    StaticShift findById(Long id);
    Set<StaticShift> findAllByIds(Set<Long> ids);
}
