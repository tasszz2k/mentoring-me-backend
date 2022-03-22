package com.labate.mentoringme.service.feedback;

import org.springframework.data.domain.Page;
import com.labate.mentoringme.dto.request.CreatedFeedbackRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.model.Feedback;

public interface FeedbackService {

  Page<Feedback> getByUserId(Long toUserId, PageCriteria pageCriteria);

  Feedback createFeedback(CreatedFeedbackRequest createdFeedbackRequest);
}
