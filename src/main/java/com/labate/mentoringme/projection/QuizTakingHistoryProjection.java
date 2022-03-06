package com.labate.mentoringme.projection;

import java.util.Date;

public interface QuizTakingHistoryProjection {
  public String getTitle();

  public Date getCreated();

  public Integer getScore();

  public Integer getNumberOfQuestion();

  public Integer getNumberOfTrue();

  public Integer getNumberOfFalse();

  public String getAuthor();

}
