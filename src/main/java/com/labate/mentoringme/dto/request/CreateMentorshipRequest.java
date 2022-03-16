package com.labate.mentoringme.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.labate.mentoringme.dto.model.ShiftDto;
import com.labate.mentoringme.model.MentorshipRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import java.util.Date;
import java.util.Set;

@Data
public class CreateMentorshipRequest {
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  private Long mentorId;
  private Long categoryId;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long createdBy;

  private String title;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date startDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date endDate;

  private Integer duration;
  // private Integer type;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private MentorshipRequest.Status status = MentorshipRequest.Status.ON_GOING;

  private Float price;
  private String detailAddress;
  private Long addressId;

  @Valid private Set<ShiftDto> shifts;
}
