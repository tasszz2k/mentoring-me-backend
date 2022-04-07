package com.labate.mentoringme.service.timetable;

import com.labate.mentoringme.constant.UserRole;
import com.labate.mentoringme.dto.mapper.ShiftMapper;
import com.labate.mentoringme.dto.mapper.TimetableMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateEventRequest;
import com.labate.mentoringme.dto.request.DeleteEventRequest;
import com.labate.mentoringme.dto.request.GetTimetableRequest;
import com.labate.mentoringme.exception.EventNotFoundException;
import com.labate.mentoringme.exception.ShiftNotFoundException;
import com.labate.mentoringme.model.MentorshipRequest;
import com.labate.mentoringme.model.Timetable;
import com.labate.mentoringme.repository.TimetableRepository;
import com.labate.mentoringme.service.mentorshiprequest.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TimetableServiceImpl implements TimetableService {
  private final TimetableRepository timetableRepository;
  private final EventService eventService;
  private final ShiftService shiftService;

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
    // find by userId and from date -> date
    var timetable = timetableRepository.findByUserIdAndConditions(userId, request);
    return timetable != null
        ? timetable
        : TimetableMapper.toEntity(timetableRepository.findTimetablePrjByUserId(userId));
  }

  @Transactional
  @Override
  public void createNewEvents(CreateEventRequest request, LocalUser localUser) {
    var userId = localUser.getUserId();
    var timetable = findByUserId(userId);

    var startDate = request.getStartDate();
    var endDate = request.getEndDate();

    var shifts = Set.copyOf(ShiftMapper.toEntities(request.getShifts()));
    var savedShifts = shiftService.saveAllShifts(null, userId, shifts);
    var basicEvents =
        eventService.createBasicEvents(startDate, endDate, new ArrayList<>(savedShifts));
    var title = request.getTitle();

    var events = eventService.createEvents(basicEvents, timetable.getId(), null, title);

    eventService.saveAll(events);
  }

  @Override
  public void deleteEvents(DeleteEventRequest request, LocalUser localUser) {
    if (request.getShiftId() != null) {
      deleteEventsByShiftId(request.getShiftId(), localUser);
    } else if (request.getEventId() != null) {
      deleteEventById(request.getEventId(), localUser);
    }
  }

  private void deleteEventsByShiftId(Long shiftId, LocalUser localUser) {
    var shift = shiftService.findById(shiftId);
    if (shift == null) {
      throw new ShiftNotFoundException("id: " + shiftId);
    }
    if (localUser.getUserId() != shift.getCreatedBy()
        && !UserRole.MANAGER_ROLES.contains(localUser.getUser().getRole())) {
      throw new AccessDeniedException("You can't delete this events");
    }

    eventService.deleteByShiftId(shiftId);
  }

  private void deleteEventById(Long eventId, LocalUser localUser) {
    var event = eventService.findById(eventId);
    if (event == null) {
      throw new EventNotFoundException("id: " + eventId);
    }
    // Fixed timetableId == userId
    if (localUser.getUserId() != event.getTimetableId()
        && !UserRole.MANAGER_ROLES.contains(localUser.getUser().getRole())) {
      throw new AccessDeniedException("You can't delete this event");
    }
    eventService.deleteById(eventId);
  }
}
