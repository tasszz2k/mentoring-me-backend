package com.labate.mentoringme.controller.v1;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.labate.mentoringme.dto.request.CreateFeedbackRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.dto.response.PageResponse;
import com.labate.mentoringme.dto.response.Paging;
import com.labate.mentoringme.service.feedback.FeedbackService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/feedbacks")
public class FeedbackController {

  private final FeedbackService feedbackService;

  @GetMapping()
  public ResponseEntity<?> getListFeedback(@Valid PageCriteria pageCriteria, Long toUserId) {
    pageCriteria.setSort(List.of("-created"));
    var pageData = feedbackService.getByUserId(toUserId, pageCriteria);
    var paging = Paging.builder().limit(pageCriteria.getLimit()).page(pageCriteria.getPage())
        .total(pageData.getTotalElements()).build();
    var pageResponse = new PageResponse(pageData.getContent(), paging);
    return BaseResponseEntity.ok(pageData);
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('USER')")
  @PostMapping()
  public ResponseEntity<?> addFeedback(@RequestBody CreateFeedbackRequest createFeedbackRequest) {
    var feedback = feedbackService.createFeedback(createFeedbackRequest);
    return BaseResponseEntity.ok(feedback);
  }

  @GetMapping("/{toUserId}/overview")
  public ResponseEntity<?> getFeedbackOverview(@PathVariable Long toUserId) {
    var feedbackOverviewResponse = feedbackService.getFeedbackOverview(toUserId);
    return BaseResponseEntity.ok(feedbackOverviewResponse);
  }


}
