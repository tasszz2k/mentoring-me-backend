package com.labate.mentoringme.service.timetable;

import com.labate.mentoringme.model.Shift;
import com.labate.mentoringme.util.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EventServiceImplTest {

  @Autowired private EventService eventService;

  @Test
  void givenDatesAndShiftWithRepeatIs1Week_whenCreateBasicEvents_thenReturnBasicEvents() {
    var startDate = DateUtils.toDate(LocalDate.of(2022, 3, 23));
    var endDate = DateUtils.toDate(LocalDate.of(2022, 5, 23));
    var shift =
        new Shift(
            null,
            null,
            null,
            DayOfWeek.FRIDAY,
            1,
            Time.valueOf("08:00:00"),
            Time.valueOf("18:00:00"),
            null,
            null,
            null);
    var actual = eventService.createBasicEvents(startDate, endDate, shift);
    System.out.println(actual);
    // TODO: Assert.assertEquals(expected, actual);
    // [Event.Basic(startTime=Fri Mar 25 08:00:00 ICT 2022, endTime=Fri Mar 25 18:00:00 ICT 2022),
    // Event.Basic(startTime=Fri Apr 01 08:00:00 ICT 2022, endTime=Fri Apr 01 18:00:00 ICT 2022),
    // Event.Basic(startTime=Fri Apr 08 08:00:00 ICT 2022, endTime=Fri Apr 08 18:00:00 ICT 2022),
    // Event.Basic(startTime=Fri Apr 15 08:00:00 ICT 2022, endTime=Fri Apr 15 18:00:00 ICT 2022),
    // Event.Basic(startTime=Fri Apr 22 08:00:00 ICT 2022, endTime=Fri Apr 22 18:00:00 ICT 2022),
    // Event.Basic(startTime=Fri Apr 29 08:00:00 ICT 2022, endTime=Fri Apr 29 18:00:00 ICT 2022),
    // Event.Basic(startTime=Fri May 06 08:00:00 ICT 2022, endTime=Fri May 06 18:00:00 ICT 2022),
    // Event.Basic(startTime=Fri May 13 08:00:00 ICT 2022, endTime=Fri May 13 18:00:00 ICT 2022),
    // Event.Basic(startTime=Fri May 20 08:00:00 ICT 2022, endTime=Fri May 20 18:00:00 ICT 2022)]
  }

  @Test
  void givenDatesAndShiftWithRepeatIsMoreThan1Week_whenCreateBasicEvents_thenReturnBasicEvents() {
    var startDate = DateUtils.toDate(LocalDate.of(2022, 3, 23));
    var endDate = DateUtils.toDate(LocalDate.of(2022, 5, 23));
    var shift =
        new Shift(
            null,
            null,
            null,
            DayOfWeek.FRIDAY,
            2,
            Time.valueOf("08:00:00"),
            Time.valueOf("18:00:00"),
            null,
            null,
            null);
    var actual = eventService.createBasicEvents(startDate, endDate, shift);
    System.out.println(actual);
    // TODO: Assert.assertEquals(expected, actual);

    // [Event.Basic(startTime=Fri Mar 25 08:00:00 ICT 2022, endTime=Fri Mar 25 18:00:00 ICT 2022),
    // Event.Basic(startTime=Fri Apr 08 08:00:00 ICT 2022, endTime=Fri Apr 08 18:00:00 ICT 2022),
    // Event.Basic(startTime=Fri Apr 22 08:00:00 ICT 2022, endTime=Fri Apr 22 18:00:00 ICT 2022),
    // Event.Basic(startTime=Fri May 06 08:00:00 ICT 2022, endTime=Fri May 06 18:00:00 ICT 2022),
    // Event.Basic(startTime=Fri May 20 08:00:00 ICT 2022, endTime=Fri May 20 18:00:00 ICT 2022)]
  }

  @Test
  void createEvent() {}
}
