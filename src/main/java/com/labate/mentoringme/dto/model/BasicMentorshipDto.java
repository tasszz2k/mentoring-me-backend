package com.labate.mentoringme.dto.model;

import com.labate.mentoringme.model.Mentorship;
import lombok.Data;

@Data
public class BasicMentorshipDto {
  private Long id;
  private UserInfo mentor;
  private CategoryDto category;
  private UserInfo createdBy;
  private String title;
  private Mentorship.Status status;
}
