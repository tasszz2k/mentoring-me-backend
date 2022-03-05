package com.labate.mentoringme.dto.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	private List<QuestionDto> questions;
	private List<QuizCategoryDto> categories;
}
