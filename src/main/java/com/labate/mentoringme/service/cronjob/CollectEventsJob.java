package com.labate.mentoringme.service.cronjob;

import com.labate.mentoringme.service.timetable.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class CollectEventsJob implements CommandLineRunner {

  private final EventService eventService;
  private final TaskSchedulingService taskSchedulingService;

  @Override
  public void run(String... args) {
    Date now = new Date();
    var events = eventService.getAllActiveEventsFrom(now);
    events.forEach(taskSchedulingService::scheduleATask);
    log.info(">> {} events scheduled", events.size());
  }
}
