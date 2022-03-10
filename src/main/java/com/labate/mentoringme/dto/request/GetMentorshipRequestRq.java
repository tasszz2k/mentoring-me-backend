package com.labate.mentoringme.dto.request;

import lombok.Value;

import java.util.Date;
import java.util.List;

@Value
public class GetMentorshipRequestRq {
  Long mentorId;
  List<Long> categoryIds;
  List<Long> addressIds;
  Date startDate;
  Date endDate;
  Float minPrice;
  Float maxPrice;
}
