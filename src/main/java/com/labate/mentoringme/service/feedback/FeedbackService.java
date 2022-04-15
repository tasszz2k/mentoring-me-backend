package com.labate.mentoringme.service.feedback;

import org.springframework.data.domain.Page;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateFeedbackRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.response.FeedbackOverviewResponse;
import com.labate.mentoringme.dto.response.FeedbackResponse;
import com.labate.mentoringme.model.Feedback;

public interface FeedbackService {

  Page<FeedbackResponse> getByUserId(Long toUserId, PageCriteria pageCriteria, LocalUser localUser);

  Feedback createFeedback(CreateFeedbackRequest createFeedbackRequest, LocalUser localUser);

  FeedbackOverviewResponse getFeedbackOverview(Long toUserId, LocalUser localUser);

  void deleteFeedback(Long id, LocalUser localUser);

}
