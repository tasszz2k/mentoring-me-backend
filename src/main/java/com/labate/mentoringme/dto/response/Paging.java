package com.labate.mentoringme.dto.response;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Paging {
  int limit;
  int page;
  long total;
}
