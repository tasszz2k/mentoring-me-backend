package com.labate.mentoringme.dto.model;

import com.labate.mentoringme.model.Class;
import lombok.Data;

@Data
public class BasicMentorshipRequestDto {
  private Long id;
  private UserInfo mentor;
  private CategoryDto category;
  private UserInfo createdBy;
  private String title;
  private Class.Status status;

}
