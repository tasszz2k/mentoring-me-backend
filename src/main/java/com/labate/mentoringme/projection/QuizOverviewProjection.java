package com.labate.mentoringme.projection;

public interface QuizOverviewProjection {
  Long getId();

  String getTitle();

  Integer getNumberOfQuestion();

  Integer getTime();

  Boolean getIsDraft();

  String getAuthor();
}
