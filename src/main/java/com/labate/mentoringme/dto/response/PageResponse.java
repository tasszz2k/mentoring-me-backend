package com.labate.mentoringme.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PageResponse {
  private Object content;
  private Paging paging;
}
