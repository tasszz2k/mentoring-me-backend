package com.labate.mentoringme.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.labate.mentoringme.model.Mentorship;
import lombok.Builder;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Builder
@Value
public class GetTimetableRequest {
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  Date fromDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  Date toDate;
}
