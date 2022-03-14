package com.labate.mentoringme.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class MentorshipDto extends BasicMentorshipDto {
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date startDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date endDate;

  private Integer duration;
  private Integer type;
  private Float price;
  private String detailAddress;
  private AddressDto address;

  private Set<ShiftDto> shifts;
  private Set<UserInfo> students;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
  private Date createdDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
  private Date modifiedDate;
}
