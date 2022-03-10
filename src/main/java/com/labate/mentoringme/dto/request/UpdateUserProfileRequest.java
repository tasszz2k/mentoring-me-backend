package com.labate.mentoringme.dto.request;

import com.labate.mentoringme.constant.Gender;
import com.labate.mentoringme.validator.ValidPhoneNumber;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class UpdateUserProfileRequest {
  @NotBlank private String fullName;
  // private String email;
  @ValidPhoneNumber private String phoneNumber;
  private String imageUrl;
  private Gender gender;
  private Date dob;
  private String school;
  private Long addressId;
  private String detailAddress;
  private String bio;
  private Float price;
  private Boolean isOnlineStudy;
  private Boolean isOfflineStudy;
  private List<Long> categoryIds = new ArrayList<>();
}
