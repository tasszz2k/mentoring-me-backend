package com.labate.mentoringme.service.favorite;

import org.springframework.data.domain.Page;
import com.labate.mentoringme.dto.QuizOverviewDto;
import com.labate.mentoringme.dto.model.UserDto;
import com.labate.mentoringme.dto.request.CreateFavoriteMentorRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.quiz.AddFavoriteQuizRequest;
import com.labate.mentoringme.model.FavoriteMentor;
import com.labate.mentoringme.model.quiz.FavoriteQuiz;

public interface FavoriteService {
  Page<QuizOverviewDto> findFavoriteQuizByUserId(PageCriteria pageCriteria);

  void deleteFavoriteQuiz(Long quizId);

  FavoriteQuiz findByQuizIdAndUserId(Long quizId);

  FavoriteQuiz addFavoriteQuiz(AddFavoriteQuizRequest addFavoriteQuizRequest);

  Page<UserDto> findFavoriteMentor(PageCriteria pageCriteria);

  void deleteFavoriteMentor(Long mentorId);

  FavoriteMentor addFavoriteMentor(CreateFavoriteMentorRequest addFavoriteMentorRequest);
}
