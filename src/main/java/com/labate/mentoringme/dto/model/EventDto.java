package com.labate.mentoringme.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class EventDto {
  private Long id;
  private String title;
  private Date startTime;
  private Date endTime;
  private Integer type;
}
