package com.labate.mentoringme.controller.v1;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.labate.mentoringme.dto.request.FindQuizRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.quiz.CreateQuizRequest;
import com.labate.mentoringme.dto.request.quiz.ResultQuizCheckingRequest;
import com.labate.mentoringme.dto.request.quiz.UpdateQuizRequest;
import com.labate.mentoringme.dto.response.ApiResponse;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.service.quizz.QuizService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/quizzes")
public class QuizController {

  private final QuizService quizService;

  @GetMapping()
  private ResponseEntity<?> getAllQuiz(@Valid PageCriteria pageCriteria,
      FindQuizRequest quizRequest) {
    return BaseResponseEntity.ok(quizService.findAllQuiz(quizRequest, pageCriteria));
  }

  @GetMapping("/{quizId}")
  private ResponseEntity<?> getQuizDetail(@PathVariable Long quizId) {
    var quiz = quizService.findById(quizId);
    if (quiz == null) {
      return ResponseEntity.badRequest().body(ApiResponse.fail(null, "Quiz not found"));
    }
    return BaseResponseEntity.ok(quiz);
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR')")
  @PostMapping()
  private ResponseEntity<?> addQuiz(@RequestBody CreateQuizRequest createQuizRequest) {
    var quiz = quizService.addQuiz(createQuizRequest);
    return BaseResponseEntity.ok(quiz);
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR')")
  @PutMapping()
  private ResponseEntity<?> updateQuiz(@RequestBody UpdateQuizRequest updateQuizRequest) {
    var quiz = quizService.updateQuiz(updateQuizRequest);
    return BaseResponseEntity.ok(quiz);
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR')")
  @GetMapping("/drafts")
  private ResponseEntity<?> getListDraftQuiz() {
    return BaseResponseEntity.ok(quizService.getListDraftQuiz());
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR')")
  @DeleteMapping("/{quizId}")
  public ResponseEntity<?> deleteCategory(@PathVariable Long quizId) {
    var quiz = quizService.findById(quizId);
    if (quiz == null) {
      return ResponseEntity.badRequest().body(ApiResponse.fail(null, "Quiz not found"));
    }
    quizService.deleteById(quizId);
    return BaseResponseEntity.ok("Category deleted successfully");
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('MENTOR', 'USER')")
  @GetMapping("/results")
  private ResponseEntity<?> getQuizTakingHistory() {
    return BaseResponseEntity.ok(quizService.getQuizTakingHistory());
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('MENTOR', 'USER')")
  @PostMapping("/results")
  private ResponseEntity<?> getQuizTakingResult(@RequestBody ResultQuizCheckingRequest request) {
    return BaseResponseEntity.ok(quizService.getQuizResult(request));
  }
}
