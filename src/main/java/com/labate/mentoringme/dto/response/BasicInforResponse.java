package com.labate.mentoringme.dto.response;

import com.labate.mentoringme.constant.Gender;
import com.labate.mentoringme.model.Role;
import lombok.Data;

import java.util.List;
@Data
public class BasicInforResponse {
  private Long id;
  private String email;
  private String fullName;
  private String phoneNumber;
  private String imageUrl;
  private Gender gender;
}
