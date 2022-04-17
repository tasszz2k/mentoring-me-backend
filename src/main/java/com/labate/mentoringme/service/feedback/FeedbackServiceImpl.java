package com.labate.mentoringme.service.feedback;

import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateFeedbackRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.response.FeedbackOverviewResponse;
import com.labate.mentoringme.dto.response.FeedbackResponse;
import com.labate.mentoringme.exception.UserAlreadyFeedbackMentorException;
import com.labate.mentoringme.model.Feedback;
import com.labate.mentoringme.repository.FeedbackRepository;
import com.labate.mentoringme.repository.ProfileRepository;
import com.labate.mentoringme.service.notification.NotificationService;
import com.labate.mentoringme.util.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

  private final FeedbackRepository feedbackRepository;

  private final ProfileRepository profileRepository;
  private final NotificationService notificationService;

  private ModelMapper modelMapper = new ModelMapper();

  @Override
  public Page<FeedbackResponse> getByUserId(
      Long toUserId, PageCriteria pageCriteria, LocalUser localUser) {
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    Long fromUserId = null;
    if (!Objects.isNull(localUser)) {
      fromUserId = localUser.getUserId();
    }
    return feedbackRepository
        .findByToUserId(toUserId, fromUserId, pageable)
        .map(
            ele -> {
              var feedbackResponse = ObjectMapperUtils.map(ele, FeedbackResponse.class);
              return feedbackResponse;
            });
  }

  @Transactional
  @Override
  public Feedback createFeedback(CreateFeedbackRequest createFeedbackRequest, LocalUser localUser) {
    var oldFeedback =
        feedbackRepository.findByToUserIdAndFromUserId(
            createFeedbackRequest.getToUserId(), localUser.getUserId());
    if (oldFeedback != null) {
      throw new UserAlreadyFeedbackMentorException("UserId: " + localUser.getUserId());
    }
    var feedback = modelMapper.map(createFeedbackRequest, Feedback.class);
    feedback.setFromUserId(localUser.getUserId());
    var userProfileOpt = profileRepository.findById(createFeedbackRequest.getToUserId());
    if (userProfileOpt.isPresent()) {
      var numberOfFeedback =
          feedbackRepository.findByToUserId(createFeedbackRequest.getToUserId()).size();
      Float newRating = (float) 0;
      if (numberOfFeedback == 0) {
        newRating = createFeedbackRequest.getRating().floatValue();
      } else {
        var currentRating = userProfileOpt.get().getRating();
        newRating =
            (currentRating * numberOfFeedback + createFeedbackRequest.getRating())
                / (numberOfFeedback + 1);
      }
      profileRepository.updateRating(newRating, createFeedbackRequest.getToUserId());
    }
    Feedback savedFeedback = feedbackRepository.save(feedback);
    return savedFeedback;
  }

  @Override
  public FeedbackOverviewResponse getFeedbackOverview(Long toUserId, LocalUser localUser) {
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
    var feedbackOverviewResponse =
        FeedbackOverviewResponse.builder()
            .overallRating(overallRating)
            .numberOfFeedback(numberOfFeedback)
            .proportionOfOneRating(calculateProportion(numberOfOneRating, numberOfFeedback))
            .proportionOfTwoRating(calculateProportion(numberOfTwoRating, numberOfFeedback))
            .proportionOfThreeRating(calculateProportion(numberOfThreeRating, numberOfFeedback))
            .proportionOfFourRating(calculateProportion(numberOfFourRating, numberOfFeedback))
            .proportionOfFiveRating(calculateProportion(numberOfFiveRating, numberOfFeedback))
            .build();
    if (!Objects.isNull(localUser)) {
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

  @Override
  public void deleteFeedback(Long id, LocalUser localUser) {
    var feedback = feedbackRepository.findByToUserIdAndFromUserId(id, localUser.getUserId());
    if (!Objects.isNull(feedback)) {
      feedbackRepository.delete(feedback);
    }
  }

  private Double calculateProportion(int value, int sampleSize) {
    if (sampleSize == 0) return (double) 0;
    var result = (double) (value * 100) / sampleSize;
    return Double.parseDouble(new DecimalFormat("#.#").format(result));
  }
}
