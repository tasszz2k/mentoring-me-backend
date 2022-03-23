package com.labate.mentoringme.dto.request;

import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReportRequest {
  private Long toUserId;

  private String reason;

  private String description;

  private MultipartFile[] images;
}
