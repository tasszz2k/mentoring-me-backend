package com.labate.mentoringme.projection;

public interface QuizProjection {
  public Long getId();

  public String getTitle();

  public String getDescription();

  public Integer getNumberOfQuestion();

  public Boolean getVisibleStatus();

  public Boolean getEditableStatus();

  public Integer getType();

  public Integer getTime();

  public Boolean getIsDraft();
}
