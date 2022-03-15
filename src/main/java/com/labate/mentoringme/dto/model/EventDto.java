package com.labate.mentoringme.dto.model;

import lombok.Value;

import java.util.Date;

@Value
public class EventDto {
  Long id;
  String title;
  String content;
  Date fromTime;
  Date toTime;
  Integer type;
}
