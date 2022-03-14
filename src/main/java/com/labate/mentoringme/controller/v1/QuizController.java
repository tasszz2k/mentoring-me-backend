package com.labate.mentoringme.controller.v1;

import java.util.ArrayList;
import java.util.Comparator;
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
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.quiz.CreateQuizRequest;
import com.labate.mentoringme.dto.request.quiz.FindQuizRequest;
import com.labate.mentoringme.dto.request.quiz.ResultQuizCheckingRequest;
import com.labate.mentoringme.dto.request.quiz.UpdateQuizOverviewRequest;
import com.labate.mentoringme.dto.request.quiz.UpdateQuizRequest;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.dto.response.PageResponse;
import com.labate.mentoringme.dto.response.Paging;
import com.labate.mentoringme.dto.response.QuizTakingHistoryResponse;
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
  public ResponseEntity<?> getQuizOverview(@PathVariable Long quizId) {
    var quiz = quizService.findById(quizId);
    if (quiz == null) {
      throw new QuizNotFoundException("id = " + quizId);
    }
    return BaseResponseEntity.ok(quizService.getQuizOverview(quizId));
  }

  @PatchMapping("/overview")
  public ResponseEntity<?> updateQuizOverview(@RequestBody UpdateQuizOverviewRequest request) {
    var quiz = quizService.findById(request.getId());
    if (quiz == null) {
      throw new QuizNotFoundException("id = " + request.getId());
    }
    return BaseResponseEntity.ok(quizService.updateQuizOverview(request));
  }

  @GetMapping()
  public ResponseEntity<?> getAllQuiz(@Valid PageCriteria pageCriteria,
      FindQuizRequest quizRequest) {


    var pageData = quizService.findAllQuiz(quizRequest, pageCriteria);
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
  public ResponseEntity<?> addQuiz(@RequestBody CreateQuizRequest createQuizRequest) {
    var quiz = quizService.addQuiz(createQuizRequest);
    return BaseResponseEntity.ok(quiz);
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR')")
  @PutMapping()
  public ResponseEntity<?> updateQuiz(@RequestBody UpdateQuizRequest updateQuizRequest) {
    var quiz = quizService.updateQuiz(updateQuizRequest);
    return BaseResponseEntity.ok(quiz);
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR')")
  @GetMapping("/drafts")
  public ResponseEntity<?> getListDraftQuiz(@Valid PageCriteria pageCriteria) {
    var pageData = quizService.getListDraftQuiz(pageCriteria);
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
  public ResponseEntity<?> getQuizTakingHistory(@Valid PageCriteria pageCriteria) {
    var pageData = quizService.getQuizTakingHistory(pageCriteria);
    var paging = Paging.builder().limit(pageCriteria.getLimit()).page(pageCriteria.getPage())
        .total(pageData.getTotalElements()).build();
    var content = new ArrayList<QuizTakingHistoryResponse>();
    pageData.getContent().forEach(item -> content.add(item));
    content.sort(Comparator.comparing(QuizTakingHistoryResponse::getCreated).reversed());
    var pageResponse = new PageResponse(content, paging);
    return BaseResponseEntity.ok(pageResponse);
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('MENTOR', 'USER')")
  @PostMapping("/results")
  public ResponseEntity<?> getQuizTakingResult(@RequestBody ResultQuizCheckingRequest request) {
    return BaseResponseEntity.ok(quizService.getQuizResult(request));
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
