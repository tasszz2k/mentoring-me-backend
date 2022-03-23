package com.labate.mentoringme.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.time.DayOfWeek;

@Data
public class ShiftDto {
  private Long id;
  private Long mentorshipId;

  @NotNull private DayOfWeek dayOfWeek;

  @Min(value = 1, message = "repeat must be greater than 0")
  @Max(value = 4, message = "repeat must be less than or equal to 4")
  private Integer repeat = 1;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
  @DateTimeFormat(pattern = "HH:mm:ss")
  private Time startTime;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
  @DateTimeFormat(pattern = "HH:mm:ss")
  private Time endTime;

  @AssertTrue(message = "end time must be after than start time")
  private boolean checkEndTimeAfterStartTime() {
    return endTime.after(startTime);
  }

}
