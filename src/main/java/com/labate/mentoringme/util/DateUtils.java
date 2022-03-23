package com.labate.mentoringme.util;

import java.sql.Time;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DateUtils {
  public static LocalDate toLocalDate(Date date) {
    Date lDate = new Date(date.getTime());
    return lDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public static List<LocalDate> getDateFromTo(LocalDate startDate, LocalDate endDate) {
    return startDate.datesUntil(endDate).collect(Collectors.toList());
  }

  public static Date toDate(LocalDate localDate, Time startTime) {
    var localDateTime = localDate.atTime(startTime.toLocalTime());
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  public static Date toDate(LocalDate localDate) {
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }
}
