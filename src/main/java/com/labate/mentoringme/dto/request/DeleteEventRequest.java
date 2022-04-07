package com.labate.mentoringme.dto.request;

import lombok.Data;

@Data
public class DeleteEventRequest {
  private Long eventId;
  private Long shiftId;
}
