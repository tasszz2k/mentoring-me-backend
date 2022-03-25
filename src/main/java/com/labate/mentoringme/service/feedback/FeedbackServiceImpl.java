package com.labate.mentoringme.service.feedback;

import java.text.DecimalFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateFeedbackRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.response.FeedbackResponse;
import com.labate.mentoringme.dto.response.PageResponse;
import com.labate.mentoringme.dto.response.Paging;
import com.labate.mentoringme.model.Feedback;
import com.labate.mentoringme.repository.FeedbackRepository;
import com.labate.mentoringme.util.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

  private final FeedbackRepository feedbackRepository;

  @Override
  public FeedbackResponse getByUserId(Long toUserId, PageCriteria pageCriteria) {
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    var pageData = feedbackRepository.findByToUserId(toUserId, pageable);
    var paging = Paging.builder().limit(pageCriteria.getLimit()).page(pageCriteria.getPage())
        .total(pageData.getTotalElements()).build();
    var pageResponse = new PageResponse(pageData.getContent(), paging);

    var feedbacks = feedbackRepository.findByToUserId(toUserId);
    Double totalRating = (double) 0;
    for (Feedback feedback : feedbacks) {
      totalRating += feedback.getRating();
    }

    var overallRating = totalRating / feedbacks.size();
    overallRating = Double.parseDouble(new DecimalFormat("#.##").format(overallRating));

    return new FeedbackResponse(pageResponse, overallRating);
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
