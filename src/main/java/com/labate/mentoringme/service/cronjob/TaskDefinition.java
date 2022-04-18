package com.labate.mentoringme.service.cronjob;

import com.labate.mentoringme.util.DateUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class TaskDefinition implements Runnable {
  private Date startTime;
  private String cronExpression;

  public TaskDefinition(Date startTime) {
    this.startTime = startTime;
    setCronExpressionByEvent();
  }

  @Override
  public void run() {
    System.out.println("Running action at: " + startTime);
  }

  public void setCronExpressionByEvent() {
    var dateTime = DateUtils.toLocalDateTime(this.startTime);
    this.cronExpression = convertLocalDateTimeToCronExpression(dateTime);
  }

  private String convertLocalDateTimeToCronExpression(LocalDateTime dateTime) {
    var minute = dateTime.getMinute();
    var hour = dateTime.getHour();
    var dayOfMonth = dateTime.getDayOfMonth();
    var month = dateTime.getMonthValue();
    var dayOfWeek = dateTime.getDayOfWeek().getValue();
    // var year = dateTime.getYear();
    return String.format("0 %s %s %s %s %s", minute, hour, dayOfMonth, month, dayOfWeek);
  }
}
