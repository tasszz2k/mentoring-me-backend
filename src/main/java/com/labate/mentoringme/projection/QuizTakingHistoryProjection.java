package com.labate.mentoringme.projection;

import java.util.Date;

public interface QuizTakingHistoryProjection {
  String getTitle();

  Date getCreated();

  Integer getScore();

  Integer getNumberOfQuestion();

  Integer getNumberOfTrue();

  Integer getNumberOfFalse();

  String getAuthor();
}
