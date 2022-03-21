package com.labate.mentoringme.service.quizz;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.labate.mentoringme.dto.QuizOverviewDto;
import com.labate.mentoringme.dto.QuizResultCheckingDto;
import com.labate.mentoringme.dto.UserSelectionDto;
import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.model.QuizDto;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.quiz.CreateQuizRequest;
import com.labate.mentoringme.dto.request.quiz.FindQuizRequest;
import com.labate.mentoringme.dto.request.quiz.ResultQuizCheckingRequest;
import com.labate.mentoringme.dto.request.quiz.UpdateQuizOverviewRequest;
import com.labate.mentoringme.dto.request.quiz.UpdateQuizRequest;
import com.labate.mentoringme.dto.response.QuizResultResponse;
import com.labate.mentoringme.dto.response.QuizTakingHistoryResponse;
import com.labate.mentoringme.model.Category;
import com.labate.mentoringme.model.quiz.Question;
import com.labate.mentoringme.model.quiz.Quiz;
import com.labate.mentoringme.model.quiz.QuizResult;
import com.labate.mentoringme.repository.FavoriteQuizRepository;
import com.labate.mentoringme.repository.QuestionRepository;
import com.labate.mentoringme.repository.QuizRepository;
import com.labate.mentoringme.repository.QuizResultRepository;
import com.labate.mentoringme.util.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizServiceImpl implements QuizService {

  private final QuizRepository quizRepository;
  private final QuestionRepository questionRepository;
  private final QuizResultRepository quizResultRepository;
  private final FavoriteQuizRepository favoriteQuizRepository;
  private final ModelMapper modelMapper = new ModelMapper();

  @Override
  public Page<QuizOverviewDto> findAllQuiz(FindQuizRequest request, PageCriteria pageCriteria) {
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    var response = quizRepository.findAllByConditions(request, pageable).map(quiz -> {
      var quizDto = modelMapper.map(quiz, QuizOverviewDto.class);
      return quizDto;
    });
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof LocalUser) {
      LocalUser localUser =
          (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      var userId = localUser.getUser().getId();
      response.getContent().forEach(item -> {
        if (item.getUserId() != null && item.getUserId() == userId) {
          item.setIsLiked(true);
        } else {
          item.setIsLiked(false);
        }
      });
    }
    return response;
  }

  @Override
  public QuizOverviewDto getQuizOverview(Long quizId) {
    var quizOpt = quizRepository.findById(quizId);
    if (quizOpt.isPresent()) {
      var quizOverview = ObjectMapperUtils.map(quizOpt.get(), QuizOverviewDto.class);
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (principal instanceof LocalUser) {
        LocalUser localUser =
            (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = localUser.getUser().getId();
        var favoriteQuiz = favoriteQuizRepository.findByUserIdAndQuizId(userId, quizId);
        quizOverview.setIsLiked(false);
        if (favoriteQuiz != null)
          quizOverview.setIsLiked(true);;
      }
      return quizOverview;
    }
    return null;
  }

  @Override
  public QuizDto findById(Long quizId) {
    var quizOpt = quizRepository.findById(quizId);
    if (quizOpt.isEmpty())
      return null;
    var quizDto = modelMapper.map(quizOpt.get(), QuizDto.class);
    return quizDto;
  }

  @Override
  public void deleteById(Long quizId) {
    quizRepository.deleteById(quizId);
  }

  @Override
  public QuizDto addQuiz(CreateQuizRequest createQuizRequest) {
    LocalUser localUser =
        (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var quiz = modelMapper.map(createQuizRequest, Quiz.class);
    quiz.setCreatedBy(localUser.getUser().getId());
    quiz.setAuthor(localUser.getUser().getFullName());
    quiz.getQuestions().forEach(question -> {
      question.setQuiz(quiz);
    });
    for (Question question : quiz.getQuestions()) {
      question.getAnswers().forEach(answer -> answer.setQuestion(question));
    }
    return modelMapper.map(questionRepository.save(quiz), QuizDto.class);
  }

  @Override
  public QuizDto updateQuiz(UpdateQuizRequest updateQuizRequest) {
    LocalUser localUser =
        (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var quiz = modelMapper.map(updateQuizRequest, Quiz.class);
    quiz.setModifiedBy(localUser.getUser().getId());
    quiz.getQuestions().forEach(question -> {
      question.setQuiz(quiz);
    });
    for (Question question : quiz.getQuestions()) {
      question.getAnswers().forEach(answer -> answer.setQuestion(question));
    }
    var oldQuiz = quizRepository.findById(quiz.getId());
    quiz.setAuthor(oldQuiz.get().getAuthor());
    quiz.setCreatedBy(oldQuiz.get().getCreatedBy());
    quiz.setCreatedDate(oldQuiz.get().getCreatedDate());
    quiz.setAuthor(oldQuiz.get().getAuthor());
    return modelMapper.map(questionRepository.save(quiz), QuizDto.class);
  }

  @Override
  public Page<QuizTakingHistoryResponse> getQuizTakingHistory(PageCriteria pageCriteria) {
    LocalUser localUser =
        (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    var userId = localUser.getUser().getId();
    var response = quizResultRepository.getQuizTakingHistory(userId, pageable).map(item -> {
      var quizTakingHistoryResponse = modelMapper.map(item, QuizTakingHistoryResponse.class);
      return quizTakingHistoryResponse;
    });
    return response;
  }

  @Override
  public QuizResultResponse getQuizResult(ResultQuizCheckingRequest request) {
    var results = questionRepository.getQuizResult(request.getQuizId()).stream().map(ele -> {
      var item = modelMapper.map(ele, QuizResultCheckingDto.class);
      return item;
    }).collect(Collectors.toList());
    var response = calculateUserResult(request, results);
    saveToQuizResult(response, request.getQuizId());
    return response;
  }

  private void saveToQuizResult(QuizResultResponse response, Long quizId) {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof LocalUser) {
      LocalUser localUser =
          (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      var quizResult = modelMapper.map(response, QuizResult.class);
      quizResult.setQuizId(quizId);
      quizResult.setUserId(localUser.getUser().getId());
      quizResult.setIsDeleted(false);
      quizResultRepository.save(quizResult);
    }
  }

  private QuizResultResponse calculateUserResult(ResultQuizCheckingRequest request,
      List<QuizResultCheckingDto> results) {
    results.sort(Comparator.comparing(QuizResultCheckingDto::getQuestionId)
        .thenComparing(QuizResultCheckingDto::getAnswerId));

    Map<Long, String> answers = new HashMap();
    int i = 0, numberOfQuestion = 0;
    while (i < results.size()) {
      numberOfQuestion++;
      Long questionId = results.get(i).getQuestionId();
      String answer = results.get(i).getAnswerId() + "-";
      if (i == results.size() - 1) {
        answers.put(questionId, answer);
        break;
      }
      while (i < results.size()) {
        i++;
        if (i == results.size()) {
          answers.put(questionId, answer);
          break;
        }
        if (results.get(i).getQuestionId() == questionId) {
          answer = answer + results.get(i).getAnswerId() + "-";
        } else {
          answers.put(questionId, answer);
          break;
        }
      }
      if (i == results.size())
        break;
    }
    var numberOfFalse = numberOfQuestion - request.getUserSelection().size();
    for (UserSelectionDto userSelection : request.getUserSelection()) {
      var questionId = userSelection.getQuestionId();
      if (userSelection.getAnswerIds() == null) {
        numberOfFalse++;
        continue;
      }
      Collections.sort(userSelection.getAnswerIds());
      String answer = "";
      for (Long answerId : userSelection.getAnswerIds()) {
        if (answer.isEmpty())
          answer = answerId + "-";
        else
          answer = answer + answerId + "-";
      }
      var trueAnswer = answers.get(questionId);
      if (answer.equals(trueAnswer) == false)
        numberOfFalse++;
    }
    var score = 100 - (numberOfFalse * 100 / numberOfQuestion);
    var response = new QuizResultResponse();
    response.setNumberOfFalse(numberOfFalse);
    response.setNumberOfQuestion(numberOfQuestion);
    response.setNumberOfTrue(numberOfQuestion - numberOfFalse);
    response.setScore(score);
    return response;
  }

  @Override
  public Page<QuizOverviewDto> getListDraftQuiz(PageCriteria pageCriteria) {
    LocalUser localUser =
        (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    var userId = localUser.getUser().getId();
    return quizRepository.findAllByCreatedByAndIsDraft(userId, true, pageable).map(quiz -> {
      var quizOverviewDto = modelMapper.map(quiz, QuizOverviewDto.class);
      return quizOverviewDto;
    });
  }

  @Override
  public QuizOverviewDto updateQuizOverview(UpdateQuizOverviewRequest request) {
    LocalUser localUser =
        (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var quiz = quizRepository.findById(request.getId()).get();
    quiz.setModifiedDate(new Date());
    quiz.setModifiedBy(localUser.getUser().getId());
    quiz.setTime(request.getTime());
    quiz.setTitle(request.getTitle());
    quiz.setIsDraft(request.getIsDraft());
    var categories = request.getCategories().stream().map(ele -> {
      var category = modelMapper.map(ele, Category.class);
      return category;
    }).collect(Collectors.toSet());
    quiz.setCategories(categories);
    return modelMapper.map(quizRepository.save(quiz), QuizOverviewDto.class);
  }

  @Override
  public void publishQuiz(Long quizId) {
    quizRepository.publishQuiz(quizId);
  }
}
