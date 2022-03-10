package com.labate.mentoringme.dto.model;

import lombok.Data;

import java.sql.Time;

@Data
public class ShiftDto {
  private Long id;
  private String code;
  private Integer dayOfWeek;
  private Time startTime;
}
