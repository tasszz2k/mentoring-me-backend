package com.labate.mentoringme.service.cronjob;

import com.google.common.base.Strings;
import com.labate.mentoringme.dto.request.PushNotificationToUserRequest;
import com.labate.mentoringme.model.Event;
import com.labate.mentoringme.model.Notification;
import com.labate.mentoringme.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskSchedulingService {

  public static final int ADVANCE_NOTICE_MINUTES = -15;
  private final TaskScheduler taskScheduler;

  Map<Long, ScheduledFuture<?>> jobsMap = new HashMap<>();

  @Async
  public void scheduleATask(Event event) {
    var userId = event.getTimetableId();
    var eventId = event.getId();
    var body =
        String.format(
            "%s\n%s - %s",
            Strings.nullToEmpty(event.getTitle()),
            DateUtils.toTimeOnlyString(event.getStartTime()),
            DateUtils.toTimeOnlyString(event.getEndTime()));

    var request =
        PushNotificationToUserRequest.builder()
            .userIds(Collections.singleton(userId))
            .body(body)
            .title("Sự kiện sắp tới")
            .objectType(Notification.ObjectType.SCHEDULE)
            .objectId(eventId)
            .build();
    var task = new TaskPushNotification(request, calStartTime(event.getStartTime()));
    scheduleATask(eventId, task, task.getCronExpression());
    log.info("Scheduled task with id: " + request);
  }

  @Async
  public void scheduleTasks(Collection<Event> events) {
    for (Event event : events) {
      scheduleATask(event);
    }
  }

  private Date calStartTime(Date startTime) {
    return DateUtils.addMinutes(startTime, ADVANCE_NOTICE_MINUTES);
  }

  @Async
  public void scheduleATask(Long jobId, Runnable tasklet, String cronExpression) {
    // log.info("Scheduling task with job id: " + jobId + " and cron expression: " +
    // cronExpression);
    ScheduledFuture<?> scheduledTask =
        taskScheduler.schedule(
            tasklet,
            new CronTrigger(cronExpression, TimeZone.getTimeZone(TimeZone.getDefault().getID())));
    jobsMap.put(jobId, scheduledTask);
  }

  @Async
  public void removeScheduledTask(Long jobId) {
    ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
    if (scheduledTask != null) {
      scheduledTask.cancel(true);
      jobsMap.put(jobId, null);
    }
  }

  @Async
  public void removeScheduledTasks(List<Long> jobIds) {
    for (Long jobId : jobIds) {
      removeScheduledTask(jobId);
    }
  }
}
