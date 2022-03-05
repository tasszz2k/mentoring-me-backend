package com.labate.mentoringme.service.quizz;

import java.util.List;

import org.springframework.data.domain.Page;

import com.labate.mentoringme.dto.model.QuizDto;
import com.labate.mentoringme.dto.model.QuizOverviewDto;
import com.labate.mentoringme.dto.request.FindQuizRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.quiz.CreateQuizRequest;
import com.labate.mentoringme.dto.request.quiz.UpdateQuizRequest;
import com.labate.mentoringme.model.quiz.Quiz;

public interface QuizService {
   Page<QuizDto> findAllQuiz(FindQuizRequest request, PageCriteria pageCriteria);
   
   QuizOverviewDto getQuizOverview(Long quizId);
   
   QuizDto findById(Long quizId);
   
   void deleteById(Long quizId);
   
   List<QuizOverviewDto> getDraftQuizByUserId(Long userId);
   
   Quiz addQuiz(CreateQuizRequest createQuizRequest);
   
   Quiz updateQuiz(UpdateQuizRequest updateQuizRequest);
   
   void saveDraftQuiz(Long quizId);
   
  // List<ViewQuizTakingHistoryResponse> getQuizTakingHistory(Long userId);
}
