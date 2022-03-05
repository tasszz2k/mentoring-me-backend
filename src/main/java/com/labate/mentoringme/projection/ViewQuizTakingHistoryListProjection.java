package com.labate.mentoringme.projection;

import java.util.Date;

public interface ViewQuizTakingHistoryListProjection {
	public String getTitle();
	public Date getCreated();
	public Integer getScore();
	public Integer getNumberOfQuestion();
}
