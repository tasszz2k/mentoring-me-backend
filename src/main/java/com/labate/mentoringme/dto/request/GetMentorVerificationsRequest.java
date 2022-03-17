package com.labate.mentoringme.dto.request;

import com.labate.mentoringme.model.MentorVerification;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Value
public class GetMentorVerificationsRequest {
  MentorVerification.Status status;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  Date fromDate;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  Date toDate;
}
