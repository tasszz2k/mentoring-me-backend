package com.labate.mentoringme.service.feedback;

import java.text.DecimalFormat;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateFeedbackRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.response.FeedbackOverviewResponse;
import com.labate.mentoringme.dto.response.FeedbackResponse;
import com.labate.mentoringme.model.Feedback;
import com.labate.mentoringme.repository.FeedbackRepository;
import com.labate.mentoringme.repository.ProfileRepository;
import com.labate.mentoringme.util.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

  private final FeedbackRepository feedbackRepository;

  private final ProfileRepository profileRepository;

  private ModelMapper modelMapper = new ModelMapper();

  @Override
  public Page<FeedbackResponse> getByUserId(Long toUserId, PageCriteria pageCriteria) {
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    return feedbackRepository.findByToUserId(toUserId, pageable).map(ele -> {
      var feedbackResponse = ObjectMapperUtils.map(ele, FeedbackResponse.class);
      return feedbackResponse;
    });
  }

  @Transactional
  @Override
  public Feedback createFeedback(CreateFeedbackRequest createFeedbackRequest) {
    LocalUser localUser =
        (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var feedback = modelMapper.map(createFeedbackRequest, Feedback.class);
    feedback.setFromUserId(localUser.getUser().getId());
    var userProfileOpt = profileRepository.findById(createFeedbackRequest.getToUserId());
    if (userProfileOpt.isPresent()) {
      var numberOfFeedback =
          feedbackRepository.findByToUserId(createFeedbackRequest.getToUserId()).size();
      Float newRating = (float) 0;
      if (numberOfFeedback == 0) {
        newRating = createFeedbackRequest.getRating().floatValue();
      } else {
        var currentRating = userProfileOpt.get().getRating();
        newRating = (currentRating * numberOfFeedback + createFeedbackRequest.getRating())
            / (numberOfFeedback + 1);
      }
      profileRepository.updateRating(newRating, createFeedbackRequest.getToUserId());
    }
    return feedbackRepository.save(feedback);
  }

  @Override
  public FeedbackOverviewResponse getFeedbackOverview(Long toUserId) {
    var feedbacks = feedbackRepository.findByToUserId(toUserId);
    Double totalRating = (double) 0;
    for (Feedback feedback : feedbacks) {
      totalRating += feedback.getRating();
    }

    var overallRating = (double) 0;
    if (feedbacks.size() > 0) {
      overallRating = totalRating / feedbacks.size();
      overallRating = Double.parseDouble(new DecimalFormat("#.#").format(overallRating));
    }

    var numberOfOneRating = 0;
    var numberOfTwoRating = 0;
    var numberOfThreeRating = 0;
    var numberOfFourRating = 0;
    var numberOfFiveRating = 0;

    for (Feedback feedback : feedbacks) {
      switch (feedback.getRating()) {
        case 1:
          numberOfOneRating++;
          break;
        case 2:
          numberOfTwoRating++;
          break;
        case 3:
          numberOfThreeRating++;
          break;
        case 4:
          numberOfFourRating++;
          break;
        case 5:
          numberOfFiveRating++;
          break;
      }
    }
    var numberOfFeedback = feedbacks.size();
    var feedbackOverviewResponse = FeedbackOverviewResponse.builder().overallRating(overallRating)
        .numberOfFeedback(numberOfFeedback)
        .proportionOfOneRating(calculateProportion(numberOfOneRating, numberOfFeedback))
        .proportionOfTwoRating(calculateProportion(numberOfTwoRating, numberOfFeedback))
        .proportionOfThreeRating(calculateProportion(numberOfThreeRating, numberOfFeedback))
        .proportionOfFourRating(calculateProportion(numberOfFourRating, numberOfFeedback))
        .proportionOfFiveRating(calculateProportion(numberOfFiveRating, numberOfFeedback)).build();

    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof LocalUser) {
      LocalUser localUser =
          (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      var user = localUser.getUser();
      var feedback = feedbackRepository.findByToUserIdAndFromUserId(toUserId, user.getId());
      if (feedback != null) {
        var feedbackResponse = modelMapper.map(feedback, FeedbackResponse.class);
        feedbackResponse.setFullName(user.getFullName());
        feedbackResponse.setImageUrl(user.getImageUrl());
        feedbackOverviewResponse.setMyFeedback(feedbackResponse);
      }
    }
    return feedbackOverviewResponse;
  }

  private Double calculateProportion(int value, int sampleSize) {
    if (sampleSize == 0)
      return (double) 0;
    var result = (double) (value * 100) / sampleSize;
    return Double.parseDouble(new DecimalFormat("#.#").format(result));
  }

}
