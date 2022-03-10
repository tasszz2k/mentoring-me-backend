package com.labate.mentoringme.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class CreateMentorshipRequestRq {
  @ApiModelProperty(hidden = true)
  private Long id;

  private Long mentorId;
  private Long categoryId;

  @ApiModelProperty(hidden = true)
  private Long createdBy;

  private String title;
  private Date startDate;
  private Date endDate;
  private Integer duration;
  private Long type;
  private Long status;
  private Float price;
  private String detailAddress;
  private Long addressId;

  private Set<Long> shiftIds;
  private Set<Integer> studentIds;
}
