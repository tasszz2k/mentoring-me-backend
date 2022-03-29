package com.labate.mentoringme.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class QuizOverviewDto {
  private Long id;
  private String title;
  private Integer numberOfQuestion;
  private Integer time;
  private Boolean isDraft;
  private String author;
  private Long createdBy;
  private Boolean isLiked;
  @JsonIgnore
  private Long userId;
  private String imageUrl;
  private String role;
}
