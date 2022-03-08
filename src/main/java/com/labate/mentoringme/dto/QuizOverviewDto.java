package com.labate.mentoringme.dto;

import lombok.Data;

@Data
public class QuizOverviewDto {
  private Long id;
  private String title;
  private Integer numberOfQuestion;
  private Boolean isDeleted;
  private Integer time;
  private Long createdBy;
  private Long modifiedBy;
  private Boolean isDraft;
  private String author;
}
