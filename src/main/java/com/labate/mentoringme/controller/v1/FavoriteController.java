package com.labate.mentoringme.controller.v1;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateFavoriteMentorRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.quiz.AddFavoriteQuizRequest;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.exception.FavoriteQuizNotFoundException;
import com.labate.mentoringme.service.favorite.FavoriteService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/favorites")
public class FavoriteController {

  private final FavoriteService favoriteService;

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('MENTOR', 'USER')")
  @GetMapping("/quizzes")
  public ResponseEntity<?> getListFavoriteQuiz(PageCriteria pageCriteria,
      @CurrentUser LocalUser localUser) {
    pageCriteria.setSort(List.of("-createdDate"));
    var response = favoriteService.findFavoriteQuizByUserId(pageCriteria, localUser);
    return BaseResponseEntity.ok(response);
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('MENTOR', 'USER')")
  @DeleteMapping("/quizzes/{quizId}")
  public ResponseEntity<?> deleteFavoriteQuiz(@PathVariable Long quizId) {
    var favoriteQuiz = favoriteService.findByQuizIdAndUserId(quizId);
    if (favoriteQuiz == null) {
      throw new FavoriteQuizNotFoundException("MSG-303",
          "FavoriteQuiz is not found! quizid = " + quizId);
    }
    favoriteService.deleteFavoriteQuiz(quizId);
    return BaseResponseEntity.ok("Favorite Quiz deleted successfully");
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('MENTOR', 'USER')")
  @PostMapping("/quizzes")
  public ResponseEntity<?> addFavoriteQuiz(
      @RequestBody AddFavoriteQuizRequest addFavoriteQuizRequest) {
    var favoriteQuiz = favoriteService.addFavoriteQuiz(addFavoriteQuizRequest);
    return BaseResponseEntity.ok(favoriteQuiz);
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('USER')")
  @GetMapping("/mentors")
  public ResponseEntity<?> getListFavoriteMentor(PageCriteria pageCriteria,
      @CurrentUser LocalUser localUser) {
    pageCriteria.setSort(List.of("-createdDate"));
    return BaseResponseEntity.ok(favoriteService.findFavoriteMentor(pageCriteria, localUser));
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('USER')")
  @DeleteMapping("/mentors/{mentorId}")
  public ResponseEntity<?> deleteFavoriteMentor(@PathVariable Long mentorId) {
    favoriteService.deleteFavoriteMentor(mentorId);
    return BaseResponseEntity.ok("Favorite Quiz deleted successfully");
  }

  @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
      paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('USER')")
  @PostMapping("/mentors")
  public ResponseEntity<?> addFavoriteMentor(
      @RequestBody CreateFavoriteMentorRequest createFavoriteMentorRequest) {
    var favoriteQuiz = favoriteService.addFavoriteMentor(createFavoriteMentorRequest);
    return BaseResponseEntity.ok(favoriteQuiz);
  }


}
