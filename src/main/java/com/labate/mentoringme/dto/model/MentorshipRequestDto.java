package com.labate.mentoringme.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.labate.mentoringme.constant.UserRole;
import com.labate.mentoringme.model.MentorshipRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MentorshipRequestDto {

  private Long id;
  private UserInfo requester;
  private UserInfo assignee;
  private UserRole requesterRole;
  private MentorshipRequest.Status status;
  private MentorshipDto mentorship;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "Asia/Bangkok")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date enrollDate;

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
