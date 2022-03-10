package com.labate.mentoringme.dto.request;

import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Value
public class GetMentorshipRequestRq {
  Long createdBy;
  Long mentorId;
  List<Long> categoryIds;
  List<Long> addressIds;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  Date fromDate;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  Date toDate;

  Float minPrice;
  Float maxPrice;

}
