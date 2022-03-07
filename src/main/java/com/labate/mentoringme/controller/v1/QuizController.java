package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.dto.request.FindQuizRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.quiz.CreateQuizRequest;
import com.labate.mentoringme.dto.request.quiz.ResultQuizCheckingRequest;
import com.labate.mentoringme.dto.request.quiz.UpdateQuizRequest;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.exception.QuizNotFoundException;
import com.labate.mentoringme.service.quizz.QuizService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/quizzes")
public class QuizController {

  private final QuizService quizService;

  @GetMapping()
  public ResponseEntity<?> getAllQuiz(
      @Valid PageCriteria pageCriteria, FindQuizRequest quizRequest) {
    return BaseResponseEntity.ok(quizService.findAllQuiz(quizRequest, pageCriteria));
  }

  @GetMapping("/{quizId}")
  public ResponseEntity<?> getQuizDetail(@PathVariable Long quizId) {
    var quiz = quizService.findById(quizId);
    if (quiz == null) {
      throw new QuizNotFoundException("id = " + quizId);
    }
    return BaseResponseEntity.ok(quiz);
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR')")
  @PostMapping()
  public ResponseEntity<?> addQuiz(@RequestBody CreateQuizRequest createQuizRequest) {
    var quiz = quizService.addQuiz(createQuizRequest);
    return BaseResponseEntity.ok(quiz);
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR')")
  @PutMapping()
  public ResponseEntity<?> updateQuiz(@RequestBody UpdateQuizRequest updateQuizRequest) {
    var quiz = quizService.updateQuiz(updateQuizRequest);
    return BaseResponseEntity.ok(quiz);
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR')")
  @GetMapping("/drafts")
  public ResponseEntity<?> getListDraftQuiz() {
    return BaseResponseEntity.ok(quizService.getListDraftQuiz());
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR')")
  @DeleteMapping("/{quizId}")
  public ResponseEntity<?> deleteCategory(@PathVariable Long quizId) {
    var quiz = quizService.findById(quizId);
    if (quiz == null) {
      throw new QuizNotFoundException("id = " + quizId);
    }
    quizService.deleteById(quizId);
    return BaseResponseEntity.ok("Category deleted successfully");
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('MENTOR', 'USER')")
  @GetMapping("/results")
  public ResponseEntity<?> getQuizTakingHistory() {
    return BaseResponseEntity.ok(quizService.getQuizTakingHistory());
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('MENTOR', 'USER')")
  @PostMapping("/results")
  public ResponseEntity<?> getQuizTakingResult(@RequestBody ResultQuizCheckingRequest request) {
    return BaseResponseEntity.ok(quizService.getQuizResult(request));
  }
}
