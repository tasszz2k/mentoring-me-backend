package com.labate.mentoringme.service.quizz;

import com.labate.mentoringme.dto.model.QuizDto;
import com.labate.mentoringme.dto.model.QuizOverviewDto;
import com.labate.mentoringme.dto.request.FindQuizRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.quiz.CreateQuizRequest;
import com.labate.mentoringme.dto.request.quiz.ResultQuizCheckingRequest;
import com.labate.mentoringme.dto.request.quiz.UpdateQuizRequest;
import com.labate.mentoringme.dto.response.QuizResultResponse;
import com.labate.mentoringme.dto.response.QuizTakingHistoryResponse;
import com.labate.mentoringme.model.quiz.Quiz;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QuizService {
  Page<QuizDto> findAllQuiz(FindQuizRequest request, PageCriteria pageCriteria);

  QuizOverviewDto getQuizOverview(Long quizId);

  QuizDto findById(Long quizId);

  void deleteById(Long quizId);

  List<QuizOverviewDto> getListDraftQuiz();

  Quiz addQuiz(CreateQuizRequest createQuizRequest);

  Quiz updateQuiz(UpdateQuizRequest updateQuizRequest);

  List<QuizTakingHistoryResponse> getQuizTakingHistory();

  QuizResultResponse getQuizResult(ResultQuizCheckingRequest request);
}
