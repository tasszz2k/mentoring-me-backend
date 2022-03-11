package com.labate.mentoringme.dto.model;

import com.labate.mentoringme.constant.MentorshipRequestStatus;
import com.labate.mentoringme.dto.mapper.MentorshipRequestMapper;
import com.labate.mentoringme.model.Category;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class MentorshipRequestDto {
  private Long id;
  private Long mentorId;
  private CategoryDto category;
  private Long createdBy;
  private String title;
  private Date startDate;
  private Date endDate;
  private Integer duration;
  private Long type;
  private MentorshipRequestStatus status;
  private Float price;
  private String detailAddress;
  private AddressDto address;

  private Set<ShiftDto> shifts;
  private Set<Integer> studentIds;

  private Date createdDate;
  private Date modifiedDate;
}
