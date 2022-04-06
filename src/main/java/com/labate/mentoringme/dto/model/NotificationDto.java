package com.labate.mentoringme.dto.model;

import lombok.Data;

import java.util.Date;

@Data
public class NotificationDto {
  private Long id;
  private String title;
  private String message;
  private boolean isRead;
  private Date createdDate;
  private Date modifiedDate;
}
