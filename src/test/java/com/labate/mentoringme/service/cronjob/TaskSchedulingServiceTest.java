package com.labate.mentoringme.service.cronjob;

import com.labate.mentoringme.model.Event;
import com.labate.mentoringme.util.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.Month;

@SpringBootTest
class TaskSchedulingServiceTest {
  @Autowired private TaskSchedulingService taskSchedulingService;

  @Test
  void scheduleATask() {
    var localTime = LocalDateTime.of(2022, Month.APRIL, 18, 14, 48);

    var event =
        Event.builder()
            .id(10L)
            .timetableId(6L)
            .mentorshipId(1L)
            .title("Sinh Học luyện thi chuyên")
            .startTime(DateUtils.toDate(localTime))
            .endTime(DateUtils.toDate(localTime.plusHours(1)))
            .build();
    taskSchedulingService.scheduleATask(event);

    try {
      Thread.sleep(100000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  // @Test
  // void removeScheduledTask() {}
}
