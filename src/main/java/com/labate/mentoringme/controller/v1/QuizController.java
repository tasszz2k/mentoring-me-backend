package com.labate.mentoringme.controller.v1;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/quizzes")
public class QuizController {

  private final QuizService quizService;

  @GetMapping()
  private ResponseEntity<?> getAllQuizBy(@Valid PageCriteria pageCriteria,
      FindQuizRequest quizRequest) {
    return BaseResponseEntity.ok(quizService.findAllQuiz(quizRequest, pageCriteria));
  }

  @GetMapping("/{quizId}")
  private ResponseEntity<?> findById(@PathVariable Long quizId) {
    var quiz = quizService.findById(quizId);
    if (quiz == null) {
      return ResponseEntity.badRequest().body(ApiResponse.fail(null, "Quiz not found"));
    }
    return BaseResponseEntity.ok(quiz);
  }

  @PostMapping()
  private ResponseEntity<?> addQuiz(@RequestBody CreateQuizRequest createQuizRequest) {
    var quiz = quizService.addQuiz(createQuizRequest);
    return BaseResponseEntity.ok(quiz);
  }

  @PutMapping()
  private ResponseEntity<?> updateQuiz(@RequestBody UpdateQuizRequest updateQuizRequest) {
    var quiz = quizService.updateQuiz(updateQuizRequest);
    return BaseResponseEntity.ok(quiz);
  }

  @GetMapping("/{userId}/drafts")
  private ResponseEntity<?> getListDraftQuiz(@PathVariable Long userId) {
    return BaseResponseEntity.ok(quizService.getDraftQuizByUserId(userId));
  }

  @PatchMapping("/{userId}/drafts")
  private ResponseEntity<?> saveDraftQuiz(@PathVariable Long userId) {
    quizService.saveDraftQuiz(userId);
    return BaseResponseEntity.ok("Save quiz draft successfully!");
  }

  @DeleteMapping("/{quizId}")
  public ResponseEntity<?> deleteCategory(@PathVariable Long quizId) {
    var quiz = quizService.findById(quizId);
    if (quiz == null) {
      return ResponseEntity.badRequest().body(ApiResponse.fail(null, "Quiz not found"));
    }
    quizService.deleteById(quizId);
    return BaseResponseEntity.ok("Category deleted successfully");
  }

  @GetMapping("/{userId}/results")
  private ResponseEntity<?> getQuizTakingHistory(@PathVariable Long userId) {
    return BaseResponseEntity.ok(quizService.getQuizTakingHistory(userId));
  }

  @PostMapping("/results")
  private ResponseEntity<?> getQuizTakingResult(@RequestBody ResultQuizCheckingRequest request) {
    return BaseResponseEntity.ok(quizService.getQuizResult(request));
  }
}
