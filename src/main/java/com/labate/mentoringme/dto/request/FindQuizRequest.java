package com.labate.mentoringme.dto.request;

import lombok.Data;

@Data
public class FindQuizRequest{
	private Integer userId;
    private Integer categoryId;
}
