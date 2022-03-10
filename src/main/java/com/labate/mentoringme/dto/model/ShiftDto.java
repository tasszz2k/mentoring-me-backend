package com.labate.mentoringme.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Data
public class ShiftDto {
  private Long id;
  private Long classId;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private DayOfWeek dayOfWeek;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime startTime;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime endTime;
}
