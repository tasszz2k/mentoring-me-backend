package com.labate.mentoringme.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class QuizTakingHistoryResponse {
  private String title;
  private Date created;
  private Integer score;
  private Integer numberOfTrue;
  private Integer numberOfFalse;
  private Integer numberOfQuestion;
  private String author;
}
