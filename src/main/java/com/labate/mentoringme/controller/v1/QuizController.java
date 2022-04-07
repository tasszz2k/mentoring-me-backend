package com.labate.mentoringme.controller.v1;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.quiz.CreateQuizRequest;
import com.labate.mentoringme.dto.request.quiz.FindQuizRequest;
import com.labate.mentoringme.dto.request.quiz.ResultQuizCheckingRequest;
import com.labate.mentoringme.dto.request.quiz.UpdateQuizDetailRequest;
import com.labate.mentoringme.dto.request.quiz.UpdateQuizOverviewRequest;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.dto.response.PageResponse;
import com.labate.mentoringme.dto.response.Paging;
import com.labate.mentoringme.exception.QuizNotFoundException;
import com.labate.mentoringme.service.quizz.QuizService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/quizzes")
public class QuizController {

  private final QuizService quizService;

  @GetMapping("/{quizId}/overview")
  public ResponseEntity<?> getQuizOverview(@PathVariable Long quizId,
      @CurrentUser LocalUser localUser) {
    var quiz = quizService.findById(quizId);
    if (quiz == null) {
      throw new QuizNotFoundException("id = " + quizId);
    }
    return BaseResponseEntity.ok(quizService.getQuizOverview(quizId, localUser));
  }

  @PatchMapping("/overview")
  public ResponseEntity<?> updateQuizOverview(@RequestBody UpdateQuizOverviewRequest request,
      @CurrentUser LocalUser localUser) {
    var quiz = quizService.findById(request.getId());
    if (quiz == null) {
      throw new QuizNotFoundException("id = " + request.getId());
    }
    return BaseResponseEntity.ok(quizService.updateQuizOverview(request, localUser));
  }

  @GetMapping()
  public ResponseEntity<?> getAllQuiz(@Valid PageCriteria pageCriteria, FindQuizRequest quizRequest,
      @CurrentUser LocalUser localUser) {
    pageCriteria.setSort(List.of("-createdDate"));
    var pageData = quizService.findAllQuiz(quizRequest, pageCriteria, localUser);
    var paging = Paging.builder().limit(pageCriteria.getLimit()).page(pageCriteria.getPage())
        .total(pageData.getTotalElements()).build();
    var pageResponse = new PageResponse(pageData.getContent(), paging);
    return BaseResponseEntity.ok(pageResponse);
  }

  @GetMapping("/{quizId}")
  public ResponseEntity<?> getQuizDetail(@PathVariable Long quizId) {
    var quiz = quizService.findById(quizId);
    if (quiz == null) {
      throw new QuizNotFoundException("id = " + quizId);
    }
    return BaseResponseEntity.ok(quiz);
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR')")
  @PostMapping()
  public ResponseEntity<?> addQuiz(@RequestBody CreateQuizRequest createQuizRequest,
      @CurrentUser LocalUser localUser) {
    var quiz = quizService.addQuiz(createQuizRequest, localUser);
    return BaseResponseEntity.ok(quiz);
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR')")
  @PutMapping()
  public ResponseEntity<?> updateQuizDetail(
      @RequestBody UpdateQuizDetailRequest updateQuizDetailRequest,
      @CurrentUser LocalUser localUser) {
    var quiz = quizService.updateQuizDetail(updateQuizDetailRequest, localUser);
    return BaseResponseEntity.ok(quiz);
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR')")
  @GetMapping("/drafts")
  public ResponseEntity<?> getListDraftQuiz(@Valid PageCriteria pageCriteria,
      @CurrentUser LocalUser localUser) {
    var pageData = quizService.getListDraftQuiz(pageCriteria, localUser);
    var paging = Paging.builder().limit(pageCriteria.getLimit()).page(pageCriteria.getPage())
        .total(pageData.getTotalElements()).build();
    var pageResponse = new PageResponse(pageData.getContent(), paging);
    return BaseResponseEntity.ok(pageResponse);
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR')")
  @DeleteMapping("/{quizId}")
  public ResponseEntity<?> deleteQuiz(@PathVariable Long quizId) {
    var quiz = quizService.findById(quizId);
    if (quiz == null) {
      throw new QuizNotFoundException("id = " + quizId);
    }
    quizService.deleteById(quizId);
    return BaseResponseEntity.ok("Quiz deleted successfully");
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('MENTOR', 'USER')")
  @GetMapping("/results")
  public ResponseEntity<?> getQuizTakingHistory(@Valid PageCriteria pageCriteria,
      @CurrentUser LocalUser localUser) {
    pageCriteria.setSort(List.of("-created"));
    var pageData = quizService.getQuizTakingHistory(pageCriteria, localUser);
    var paging = Paging.builder().limit(pageCriteria.getLimit()).page(pageCriteria.getPage())
        .total(pageData.getTotalElements()).build();
    var pageResponse = new PageResponse(pageData.getContent(), paging);
    return BaseResponseEntity.ok(pageResponse);
  }

  @PostMapping("/results")
  public ResponseEntity<?> getQuizTakingResult(@RequestBody ResultQuizCheckingRequest request,
      @CurrentUser LocalUser localUser) {
    return BaseResponseEntity.ok(quizService.getQuizResult(request, localUser));
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('MENTOR', 'ADMIN', 'MODERATOR')")
  @PatchMapping("/{quizId}")
  public ResponseEntity<?> publishQuiz(@PathVariable Long quizId) {
    quizService.publishQuiz(quizId);
    return BaseResponseEntity.ok("Publish quiz successfully");
  }
}
