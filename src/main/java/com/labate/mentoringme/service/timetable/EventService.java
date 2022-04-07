package com.labate.mentoringme.service.timetable;

import com.labate.mentoringme.model.Event;
import com.labate.mentoringme.model.Shift;

import java.util.Date;
import java.util.List;

public interface EventService {

  List<Event.Basic> createBasicEvents(Date fromDate, Date toDate, List<Shift> shifts);

  List<Event> createEvents(
      List<Event.Basic> basicEvents, Long timetableId, Long mentorshipId, String title);

  List<Event> createEvents(
      List<Event.Basic> basicEvents, List<Long> timetableIds, Long mentorshipId, String title);

  List<Event.Basic> createBasicEvents(Date fromDate, Date toDate, Shift shift);

  Event createEvent(Event.Basic basicEvent, Long timetableId, Long mentorshipId, String title);

  void save(List<Event> events);

  void saveAll(List<Event> events);

  Event findById(Long eventId);

  void deleteById(Long eventId);

  List<Event> findByShiftId(Long shiftId);

  void deleteByShiftId(Long shiftId);
}
