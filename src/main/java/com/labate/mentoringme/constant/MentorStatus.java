package com.labate.mentoringme.constant;

public enum MentorStatus {
  REGISTERED("Registered"),
  IN_PROGRESS("In Progress"),
  ACCEPTED("Accepted"),
  REJECTED("Rejected");

  private final String value;

  MentorStatus(final String mentorStatus) {
    this.value = mentorStatus;
  }

  public String getValue() {
    return value;
  }
}
