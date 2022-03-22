package com.labate.mentoringme.service.feedback;

import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateFeedbackRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.model.Feedback;
import com.labate.mentoringme.repository.FeedbackRepository;
import com.labate.mentoringme.util.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

  private final FeedbackRepository feedbackRepository;

  @Override
  public Page<Feedback> getByUserId(Long toUserId, PageCriteria pageCriteria) {
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    return feedbackRepository.findByToUserId(toUserId, pageable);
  }

  @Override
  public Feedback createFeedback(CreateFeedbackRequest createFeedbackRequest) {
    LocalUser localUser =
        (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var feedback = ObjectMapperUtils.map(createFeedbackRequest, Feedback.class);
    feedback.setFromUserId(localUser.getUser().getId());
    return feedbackRepository.save(feedback);
  }

}
