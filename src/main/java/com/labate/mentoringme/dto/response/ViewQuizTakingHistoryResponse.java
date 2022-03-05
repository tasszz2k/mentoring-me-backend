package com.labate.mentoringme.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class ViewQuizTakingHistoryResponse {
    private String title;
    private Date created;
    private Integer score;
    private Integer numberOfQuestion;
    private String name;
}
