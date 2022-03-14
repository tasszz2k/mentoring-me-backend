package com.labate.mentoringme.dto.response;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizTakingHistoryResponse {
  private String title;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
  private Date created;
  private Integer score;
  private Integer numberOfTrue;
  private Integer numberOfFalse;
  private Integer numberOfQuestion;
  private String author;
}
