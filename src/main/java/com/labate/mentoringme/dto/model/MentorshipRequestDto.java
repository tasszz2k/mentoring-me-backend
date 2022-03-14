package com.labate.mentoringme.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.labate.mentoringme.model.MentorshipRequest;
import com.labate.mentoringme.model.Role;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MentorshipRequestDto {

  private Long id;
  private MentorshipDto mentorship;
  private Long requesterId;
  private Long assigneeId;
  private Role RequesterRole;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
  private Date enrollDate;

  private MentorshipRequest.Status status;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
  private Date createdDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
  private Date modifiedDate;
}
