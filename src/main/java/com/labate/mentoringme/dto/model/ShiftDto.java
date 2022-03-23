package com.labate.mentoringme.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.time.DayOfWeek;

@Data
public class ShiftDto {
  private Long id;
  private Long mentorshipId;

  @NotNull private DayOfWeek dayOfWeek;
  private Integer repeat = 1;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
  @DateTimeFormat(pattern = "HH:mm:ss")
  private Time startTime;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
  @DateTimeFormat(pattern = "HH:mm:ss")
  private Time endTime;
}
