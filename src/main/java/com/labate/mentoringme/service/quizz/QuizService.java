package com.labate.mentoringme.service.quizz;

import org.springframework.data.domain.Page;
import com.labate.mentoringme.dto.QuizOverviewDto;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.model.QuizDetailDto;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.quiz.CreateQuizRequest;
import com.labate.mentoringme.dto.request.quiz.FindQuizRequest;
import com.labate.mentoringme.dto.request.quiz.ResultQuizCheckingRequest;
import com.labate.mentoringme.dto.request.quiz.UpdateQuizDetailRequest;
import com.labate.mentoringme.dto.request.quiz.UpdateQuizOverviewRequest;
import com.labate.mentoringme.dto.response.QuizFavoriteResponse;
import com.labate.mentoringme.dto.response.QuizOverviewResponse;
import com.labate.mentoringme.dto.response.QuizResponse;
import com.labate.mentoringme.dto.response.QuizResultResponse;
import com.labate.mentoringme.dto.response.QuizTakingHistoryResponse;

public interface QuizService {
  Page<QuizFavoriteResponse> findAllQuiz(FindQuizRequest request, PageCriteria pageCriteria,
      LocalUser localUser);

  QuizOverviewResponse getQuizOverview(Long quizId, LocalUser localUser);

  QuizOverviewDto updateQuizOverview(UpdateQuizOverviewRequest request);

  QuizDetailDto findById(Long quizId);

  void deleteById(Long quizId);

  Page<QuizOverviewDto> getListDraftQuiz(PageCriteria pageCriteria);

  QuizResponse addQuiz(CreateQuizRequest createQuizRequest, LocalUser localUser);

  QuizDetailDto updateQuizDetail(UpdateQuizDetailRequest updateQuizDetailRequest,
      LocalUser localUser);

  Page<QuizTakingHistoryResponse> getQuizTakingHistory(PageCriteria pageCriteria,
      LocalUser localUser);

  QuizResultResponse getQuizResult(ResultQuizCheckingRequest request, LocalUser localUser);

  void publishQuiz(Long quizId);
}
