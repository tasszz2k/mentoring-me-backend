package com.labate.mentoringme.service.quizz;

import org.springframework.data.domain.Page;
import com.labate.mentoringme.dto.QuizOverviewDto;
import com.labate.mentoringme.dto.model.QuizDto;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.quiz.CreateQuizRequest;
import com.labate.mentoringme.dto.request.quiz.FindQuizRequest;
import com.labate.mentoringme.dto.request.quiz.ResultQuizCheckingRequest;
import com.labate.mentoringme.dto.request.quiz.UpdateQuizOverviewRequest;
import com.labate.mentoringme.dto.request.quiz.UpdateQuizRequest;
import com.labate.mentoringme.dto.response.QuizResultResponse;
import com.labate.mentoringme.dto.response.QuizTakingHistoryResponse;

public interface QuizService {
  Page<QuizOverviewDto> findAllQuiz(FindQuizRequest request, PageCriteria pageCriteria);

  QuizOverviewDto getQuizOverview(Long quizId);

  QuizOverviewDto updateQuizOverview(UpdateQuizOverviewRequest request);

  QuizDto findById(Long quizId);

  void deleteById(Long quizId);

  Page<QuizOverviewDto> getListDraftQuiz(PageCriteria pageCriteria);

  QuizDto addQuiz(CreateQuizRequest createQuizRequest);

  QuizDto updateQuiz(UpdateQuizRequest updateQuizRequest);

  Page<QuizTakingHistoryResponse> getQuizTakingHistory(PageCriteria pageCriteria);

  QuizResultResponse getQuizResult(ResultQuizCheckingRequest request);

  void publishQuiz(Long quizId);
}
