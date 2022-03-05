package com.labate.mentoringme.dto.model;

import lombok.Data;

@Data
public class QuizOverviewDto {
	private Long id;
	private Long userId;
	private String title;
	private String description;
	private Integer numberOfQuestion;
	private Boolean isDeleted = false;
	private Integer time;
	private Boolean isDraft;
}
