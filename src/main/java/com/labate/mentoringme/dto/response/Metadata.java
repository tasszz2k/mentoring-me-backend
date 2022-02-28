package com.labate.mentoringme.dto.response;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Metadata {
  int limit;
  int page;
  long total;
}
