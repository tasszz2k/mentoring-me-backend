package com.labate.mentoringme.service.timetable;

import com.labate.mentoringme.model.Timetable;
import com.labate.mentoringme.repository.EventRepository;
import com.labate.mentoringme.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TimetableServiceImpl implements TimetableService {
  private final TimetableRepository timetableRepository;
  private final EventRepository eventRepository;

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
}
