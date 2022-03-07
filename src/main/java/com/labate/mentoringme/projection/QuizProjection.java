package com.labate.mentoringme.projection;

public interface QuizProjection {
  Long getId();

  String getTitle();

  String getDescription();

  Integer getNumberOfQuestion();

  Boolean getVisibleStatus();

  Boolean getEditableStatus();

  Integer getType();

  Integer getTime();

  Boolean getIsDraft();

  String getAuthor();
}
