package com.labate.mentoringme.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.labate.mentoringme.model.MentorVerification;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MentorVerificationDto {
  private Long id;

  private UserInfo mentor;
  private UserInfo moderator;

  private MentorVerification.Status status;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "Asia/Bangkok")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date verifiedDate;

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
