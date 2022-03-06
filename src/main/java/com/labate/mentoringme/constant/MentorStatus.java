package com.labate.mentoringme.constant;

public enum MentorStatus {
	REGISTERED("Registered"),
	IN_PROGESS("In Progress"), 
	ACCEPTED("Accepted"), 
	REJECTED("Rejected");

	private final String value;

	public String getValue() {
		return value;
	}

	MentorStatus(final String mentorStatus) {
		this.value = mentorStatus;
	}
}
