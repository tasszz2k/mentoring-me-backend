package com.labate.mentoringme.dto.projection;

public interface QuizOverviewProjection {

  Long getId();

  String getTitle();

  Integer getNumberOfQuestion();

  Integer getTime();

  Boolean getIsDraft();

  String getAuthor();

  Long getAuthorId();
}
