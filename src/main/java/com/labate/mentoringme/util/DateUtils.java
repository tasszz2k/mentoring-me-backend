package com.labate.mentoringme.util;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DateUtils {
  public static SimpleDateFormat SDF_TIME_ONLY = new SimpleDateFormat("HH:mm");
  public static final long MINUTE = 60 * 1000; // in milli-seconds.

  public static LocalDate toLocalDate(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public static LocalDateTime toLocalDateTime(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
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

  public static Date toDate(LocalDateTime localDate) {
    return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
  }

  public static String toTimeOnlyString(Date date) {
    return SDF_TIME_ONLY.format(date);
  }

  public static Date addMinutes(Date date, int minutes) {
    return new Date(date.getTime() + minutes * MINUTE);
  }
}
