package com.labate.mentoringme.dto.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class MentorshipRequestDto extends BasicMentorshipRequestDto {
  private Date startDate;
  private Date endDate;
  private Integer duration;
  private Integer type;
  private Float price;
  private String detailAddress;
  private AddressDto address;

  private Set<ShiftDto> shifts;
  private Set<UserInfo> students;

  private Date createdDate;
  private Date modifiedDate;
}
