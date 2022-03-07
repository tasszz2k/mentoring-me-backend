package com.labate.mentoringme.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {
  private Long id;
  private Long userId;
  private String title;
  private String description;
  private Integer numberOfQuestion;
  private Boolean isDeleted = false;
  private Integer time;
  private Long createdBy;
  private Long modifiedBy;
  private Boolean isDraft;
  private String author;
  private List<QuestionDto> questions;
  private List<QuizCategoryDto> categories;
}
