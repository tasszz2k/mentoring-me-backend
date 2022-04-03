package com.labate.mentoringme.service.favorite;

import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateFavoriteMentorRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.quiz.AddFavoriteQuizRequest;
import com.labate.mentoringme.dto.response.PageResponse;
import com.labate.mentoringme.model.FavoriteMentor;
import com.labate.mentoringme.model.quiz.FavoriteQuiz;

public interface FavoriteService {
  PageResponse findFavoriteQuizByUserId(PageCriteria pageCriteria, LocalUser localUser);

  void deleteFavoriteQuiz(Long quizId, LocalUser localUser);

  FavoriteQuiz findByQuizIdAndUserId(Long quizId, LocalUser localUser);

  FavoriteQuiz addFavoriteQuiz(AddFavoriteQuizRequest addFavoriteQuizRequest, LocalUser localUser);

  PageResponse findFavoriteMentor(PageCriteria pageCriteria, LocalUser localUser);

  void deleteFavoriteMentor(Long mentorId, LocalUser localUser);

  FavoriteMentor addFavoriteMentor(CreateFavoriteMentorRequest addFavoriteMentorRequest,
      LocalUser localUser);
}
