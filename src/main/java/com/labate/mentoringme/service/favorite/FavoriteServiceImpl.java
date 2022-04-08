package com.labate.mentoringme.service.favorite;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.mapper.UserMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateFavoriteMentorRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.quiz.AddFavoriteQuizRequest;
import com.labate.mentoringme.dto.response.PageResponse;
import com.labate.mentoringme.dto.response.Paging;
import com.labate.mentoringme.dto.response.QuizFavoriteResponse;
import com.labate.mentoringme.model.FavoriteMentor;
import com.labate.mentoringme.model.quiz.FavoriteQuiz;
import com.labate.mentoringme.repository.FavoriteMentorRepository;
import com.labate.mentoringme.repository.FavoriteQuizRepository;
import com.labate.mentoringme.repository.QuizRepository;
import com.labate.mentoringme.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

  private final FavoriteQuizRepository favoriteQuizRepository;

  private final FavoriteMentorRepository favoriteMentorRepository;

  private final UserRepository userRepository;

  private final QuizRepository quizRepository;

  private final ModelMapper modelMapper = new ModelMapper();

  @Override
  public PageResponse findFavoriteQuizByUserId(PageCriteria pageCriteria, LocalUser localUser) {
    var userId = localUser.getUserId();
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    var favoriteQuizzes = favoriteQuizRepository.findAllByUserId(userId, pageable);
    List<Long> quizIds = new ArrayList();
    for (FavoriteQuiz favoriteQuiz : favoriteQuizzes.getContent()) {
      quizIds.add(favoriteQuiz.getQuizId());
    }
    var response = quizRepository.findAllByIds(quizIds).stream().map(quiz -> {
      var quizResponse = modelMapper.map(quiz, QuizFavoriteResponse.class);
      quizResponse.setIsLiked(true);
      return quizResponse;
    }).collect(Collectors.toList());

    var paging = Paging.builder().limit(pageCriteria.getLimit()).page(pageCriteria.getPage())
        .total(favoriteQuizzes.getTotalElements()).build();
    var pageResponse = new PageResponse(response, paging);
    return pageResponse;
  }



  @Override
  public FavoriteQuiz findByQuizIdAndUserId(Long quizId, LocalUser localUser) {
    var userId = localUser.getUserId();
    return favoriteQuizRepository.findByUserIdAndQuizId(userId, quizId);
  }

  @Override
  public void deleteFavoriteQuiz(Long quizId, LocalUser localUser) {
    var userId = localUser.getUserId();
    favoriteQuizRepository.deleteFavoriteQuiz(userId, quizId);
  }



  @Override
  public FavoriteQuiz addFavoriteQuiz(AddFavoriteQuizRequest addFavoriteQuizRequest,
      LocalUser localUser) {
    var userId = localUser.getUserId();
    var favoriteQuiz = new FavoriteQuiz();
    favoriteQuiz.setQuizId(addFavoriteQuizRequest.getQuizId());
    favoriteQuiz.setUserId(localUser.getUserId());
    return favoriteQuizRepository.save(favoriteQuiz);
  }



  @Override
  public PageResponse findFavoriteMentor(PageCriteria pageCriteria, LocalUser localUser) {
    var studentId = localUser.getUserId();
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    var favoriteMentors = favoriteMentorRepository.findAllByStudentId(studentId, pageable);
    List<Long> userIds = new ArrayList();
    for (FavoriteMentor favoriteMentor : favoriteMentors.getContent()) {
      userIds.add(favoriteMentor.getMentorId());
    }
    var mentors = userRepository.findAllById(userIds);
    var mentorInfos =
        mentors.stream().map(UserMapper::buildUserDetails).collect(Collectors.toList());
    mentorInfos.forEach(item -> item.setIsLiked(true));
    var paging = Paging.builder().limit(pageCriteria.getLimit()).page(pageCriteria.getPage())
        .total(favoriteMentors.getTotalElements()).build();
    var pageResponse = new PageResponse(mentorInfos, paging);
    return pageResponse;
  }



  @Override
  public void deleteFavoriteMentor(Long mentorId, LocalUser localUser) {
    var userId = localUser.getUserId();
    favoriteMentorRepository.deleteFavoriteMentor(userId, mentorId);
  }



  @Override
  public FavoriteMentor addFavoriteMentor(CreateFavoriteMentorRequest createFavoriteMentorRequest,
      LocalUser localUser) {
    var userId = localUser.getUserId();
    var favoriteMentor = new FavoriteMentor();
    favoriteMentor.setMentorId(createFavoriteMentorRequest.getMentorId());
    favoriteMentor.setStudentId(userId);
    return favoriteMentorRepository.save(favoriteMentor);
  }



  @Override
  public FavoriteMentor findByStudentIdAndMentorId(Long mentorId, LocalUser localUser) {
    var studentId = localUser.getUserId();
    return favoriteMentorRepository.findByStudentIdAndMentorId(studentId, mentorId);
  }

}
