package com.labate.mentoringme.service.timetable;

import com.labate.mentoringme.model.Event;
import com.labate.mentoringme.model.Shift;
import com.labate.mentoringme.repository.EventRepository;
import com.labate.mentoringme.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
  private final EventRepository eventRepository;

  @Override
  public List<Event.Basic> createBasicEvents(Date fromDate, Date toDate, List<Shift> shifts) {
    return shifts.stream()
        .map(shift -> createBasicEvents(fromDate, toDate, shift))
        .filter(Objects::nonNull)
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  @Override
  public List<Event> createEvents(
      List<Event.Basic> basicEvents, Long timetableId, Long mentorshipId, String title) {
    return basicEvents.parallelStream()
        .map(basicEvent -> createEvent(basicEvent, timetableId, mentorshipId, title))
        .collect(Collectors.toList());
  }

  @Override
  public List<Event> createEvents(
      List<Event.Basic> basicEvents, List<Long> timetableIds, Long mentorshipId, String title) {
    return timetableIds.stream()
        .map(timetableId -> createEvents(basicEvents, timetableId, mentorshipId, title))
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  @Override
  public void save(List<Event> events) {}

  @Override
  public void saveAll(List<Event> events) {
    eventRepository.saveAll(events);
  }

  @Override
  public List<Event.Basic> createBasicEvents(Date startDate, Date endDate, Shift shift) {
    var start = DateUtils.toLocalDate(startDate);
    var end = DateUtils.toLocalDate(endDate);
    var lDates = DateUtils.getDateFromTo(start, end);
    return lDates.stream()
        .map(
            date -> {
              var dayOfWeek = date.getDayOfWeek();
              if (shift.getDayOfWeek().equals(dayOfWeek)) {
                return createBasicEvent(date, shift);
              }
              return null;
            })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  public Event createEvent(
      Event.Basic basicEvent, Long timetableId, Long mentorshipId, String title) {
    return Event.builder()
        .timetableId(timetableId)
        .mentorshipId(mentorshipId)
        .startTime(basicEvent.getStartTime())
        .endTime(basicEvent.getEndTime())
        .title(title)
        .build();
  }

  private Event.Basic createBasicEvent(LocalDate date, Shift shift) {
    var startTime = shift.getStartTime();
    var endTime = shift.getEndTime();
    var start = DateUtils.toDate(date, startTime);
    var end = DateUtils.toDate(date, endTime);
    return new Event.Basic(start, end);
  }
}
