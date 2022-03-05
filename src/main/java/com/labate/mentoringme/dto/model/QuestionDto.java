package com.labate.mentoringme.dto.model;

import java.util.List;

import lombok.Data;

@Data
public class QuestionDto {
	private Long id;
	private String question;
	private String description;
	private Boolean isMultipleChoice;
	private Boolean isDeleted = false;
	private List<AnswerDto> answers;
	
}
