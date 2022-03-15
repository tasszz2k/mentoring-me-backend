package com.labate.mentoringme.service.timetable;

import com.labate.mentoringme.model.MentorshipRequest;
import com.labate.mentoringme.model.Timetable;

public interface TimetableService {
  Timetable findById(Long timetableId);

  Timetable save(Timetable timetable);

  Timetable createNewTimetable(Long userId, String name);

  Timetable findByUserId(Long userId);

  void fillMentorshipEvents(MentorshipRequest mentorshipRequest);
}
