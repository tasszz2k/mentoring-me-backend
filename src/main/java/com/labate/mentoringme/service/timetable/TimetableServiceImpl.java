package com.labate.mentoringme.service.timetable;

import com.labate.mentoringme.dto.request.GetTimetableRequest;
import com.labate.mentoringme.model.MentorshipRequest;
import com.labate.mentoringme.model.Timetable;
import com.labate.mentoringme.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TimetableServiceImpl implements TimetableService {
  private final TimetableRepository timetableRepository;
  private final EventService eventService;

  @Override
  public Timetable findById(Long timetableId) {
    return timetableRepository.findById(timetableId).orElse(null);
  }

  @Override
  public Timetable save(Timetable timetable) {
    return timetableRepository.save(timetable);
  }

  @Override
  public Timetable createNewTimetable(Long userId, String name) {
    var timetable = new Timetable();
    timetable.setUserId(userId);
    timetable.setName(name);
    return save(timetable);
  }

  @Override
  public Timetable findByUserId(Long userId) {
    return timetableRepository.findByUserId(userId);
  }

  @Override
  public void fillMentorshipEvents(MentorshipRequest mentorshipRequest) {
    var mentorshipId = mentorshipRequest.getMentorship().getId();

    var mentorId = mentorshipRequest.getMentorship().getMentorId();
    var studentId = mentorshipRequest.getMentorship().getCreatedBy();

    var mentorTimetableId = findByUserId(mentorId).getId();
    var studentTimetableId = findByUserId(studentId).getId();

    var startDate = mentorshipRequest.getMentorship().getStartDate();
    var endDate = mentorshipRequest.getMentorship().getEndDate();
    var shifts = mentorshipRequest.getMentorship().getShifts();

    var basicEvents = eventService.createBasicEvents(startDate, endDate, new ArrayList<>(shifts));
    var timetableIds = List.of(mentorTimetableId, studentTimetableId);

    String mentorshipTitle = mentorshipRequest.getMentorship().getTitle();

    var events =
        eventService.createEvents(basicEvents, timetableIds, mentorshipId, mentorshipTitle);

    eventService.saveAll(events);
  }

  @Override
  public Timetable findByUserId(Long userId, GetTimetableRequest request) {
    // TODO: find by userId and from date -> date
    return timetableRepository.findByUserId(userId);
  }
}
