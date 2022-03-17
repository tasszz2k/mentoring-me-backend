package com.labate.mentoringme.service.favorite;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.labate.mentoringme.dto.QuizOverviewDto;
import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.quiz.AddFavoriteQuizRequest;
import com.labate.mentoringme.model.quiz.FavoriteQuiz;
import com.labate.mentoringme.repository.FavoriteQuizRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

  private final FavoriteQuizRepository favoriteQuizRepository;

  private final ModelMapper modelMapper = new ModelMapper();

  @Override
  public Page<QuizOverviewDto> findFavoriteQuizByUserId(PageCriteria pageCriteria) {
    LocalUser localUser =
        (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var userId = localUser.getUser().getId();
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    var response = favoriteQuizRepository.findAllByUserId(userId, pageable).map(quiz -> {
      var quizDto = modelMapper.map(quiz, QuizOverviewDto.class);
      return quizDto;
    });
    return response;
  }



  @Override
  public FavoriteQuiz findByQuizIdAndUserId(Long quizId) {
    LocalUser localUser =
        (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var userId = localUser.getUser().getId();
    return favoriteQuizRepository.findByUserIdAndQuizId(userId, quizId);
  }

  @Override
  public void deleteFavoriteQuiz(Long quizId) {
    LocalUser localUser =
        (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var userId = localUser.getUser().getId();
    favoriteQuizRepository.deleteFavoriteQuiz(userId, quizId);
  }



  @Override
  public FavoriteQuiz addFavoriteQuiz(AddFavoriteQuizRequest addFavoriteQuizRequest) {
    LocalUser localUser =
        (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var userId = localUser.getUser().getId();
    var favoriteQuiz = new FavoriteQuiz();
    favoriteQuiz.setQuizId(addFavoriteQuizRequest.getQuizId());
    favoriteQuiz.setUserId(userId);
    return favoriteQuizRepository.save(favoriteQuiz);
  }

}
