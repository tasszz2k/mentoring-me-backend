package com.labate.mentoringme.service.feedback;

import com.labate.mentoringme.dto.request.CreateFeedbackRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.response.FeedbackResponse;
import com.labate.mentoringme.model.Feedback;

public interface FeedbackService {

  FeedbackResponse getByUserId(Long toUserId, PageCriteria pageCriteria);

  Feedback createFeedback(CreateFeedbackRequest createFeedbackRequest);
}
