package com.labate.mentoringme.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.labate.mentoringme.model.Notification;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class NotificationDto {
  private Long id;
  private String title;
  private String body;
  private boolean isRead;
  private Notification.ObjectType objectType;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "Asia/Bangkok")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createdDate;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "Asia/Bangkok")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date modifiedDate;
}
