package com.labate.mentoringme.service.timetable;

import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateEventRequest;
import com.labate.mentoringme.dto.request.DeleteEventRequest;
import com.labate.mentoringme.dto.request.GetTimetableRequest;
import com.labate.mentoringme.model.MentorshipRequest;
import com.labate.mentoringme.model.Timetable;

public interface TimetableService {
  Timetable findById(Long timetableId);

  Timetable save(Timetable timetable);

  Timetable createNewTimetable(Long userId, String name);

  Timetable findByUserId(Long userId);

  void fillMentorshipEvents(MentorshipRequest mentorshipRequest);

  Timetable findByUserId(Long userId, GetTimetableRequest request);

  void createNewEvents(CreateEventRequest request, LocalUser localUser);

  void deleteEvents(DeleteEventRequest request, LocalUser localUser);
}
