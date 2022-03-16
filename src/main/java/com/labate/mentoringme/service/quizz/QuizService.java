package com.labate.mentoringme.service.quizz;

import com.labate.mentoringme.dto.QuizOverviewDto;
import com.labate.mentoringme.dto.model.QuizDto;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.quiz.*;
import com.labate.mentoringme.dto.response.QuizResultResponse;
import com.labate.mentoringme.dto.response.QuizTakingHistoryResponse;
import com.labate.mentoringme.model.quiz.Quiz;
import org.springframework.data.domain.Page;

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
