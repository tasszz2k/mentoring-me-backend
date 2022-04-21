package com.labate.mentoringme.dto.model;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackDto {
  private String toUserId;
  private String fullName;
  private String imageUrl;
  private Integer rating;
  private String comment;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "Asia/Bangkok")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date created;
}
