package com.labate.mentoringme.service.quizz;

import com.labate.mentoringme.dto.QuizResultCheckingDto;
import com.labate.mentoringme.dto.UserSelectionDto;
import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.model.QuizDto;
import com.labate.mentoringme.dto.model.QuizOverviewDto;
import com.labate.mentoringme.dto.request.FindQuizRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.quiz.CreateQuizRequest;
import com.labate.mentoringme.dto.request.quiz.ResultQuizCheckingRequest;
import com.labate.mentoringme.dto.request.quiz.UpdateQuizRequest;
import com.labate.mentoringme.dto.response.QuizResultResponse;
import com.labate.mentoringme.dto.response.QuizTakingHistoryResponse;
import com.labate.mentoringme.model.quiz.Question;
import com.labate.mentoringme.model.quiz.Quiz;
import com.labate.mentoringme.model.quiz.QuizResult;
import com.labate.mentoringme.repository.QuestionRepository;
import com.labate.mentoringme.repository.QuizRepository;
import com.labate.mentoringme.repository.QuizResultRepository;
import com.labate.mentoringme.util.ObjectMapperUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuizServiceImpl implements QuizService {

  private final QuizRepository quizRepository;

  private final QuestionRepository questionRepository;

  private final QuizResultRepository quizResultRepository;

  private final ModelMapper modelMapper = new ModelMapper();

  @Override
  public Page<QuizDto> findAllQuiz(FindQuizRequest request, PageCriteria pageCriteria) {
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    var response =
        quizRepository
            .findAllByConditions(request, pageable)
            .map(
                quiz -> {
                  var quizDto = modelMapper.map(quiz, QuizDto.class);
                  return quizDto;
                });
    return response;
  }

  @Override
  public QuizOverviewDto getQuizOverview(Long quizId) {
    var quizOpt = quizRepository.findById(quizId);
    if (quizOpt.isPresent()) {
      var quizOverview = ObjectMapperUtils.map(quizOpt.get(), QuizOverviewDto.class);
      return quizOverview;
    }
    return null;
  }

  @Override
  public QuizDto findById(Long quizId) {
    var quizOpt = quizRepository.findById(quizId);
    if (quizOpt.isEmpty()) return null;
    var quizDto = modelMapper.map(quizOpt.get(), QuizDto.class);
    return quizDto;
  }

  @Override
  public void deleteById(Long quizId) {
    quizRepository.deleteById(quizId);
  }

  @Override
  public Quiz addQuiz(CreateQuizRequest createQuizRequest) {
    LocalUser localUser =
        (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var quiz = modelMapper.map(createQuizRequest, Quiz.class);
    quiz.setCreatedBy(localUser.getUser().getId());
    quiz.setCreatedDate(new Date());
    quiz.setModifiedDate(new Date());
    quiz.setAuthor(localUser.getUser().getFullName());
    quiz.getQuestions()
        .forEach(
            question -> {
              question.setQuiz(quiz);
            });
    for (Question question : quiz.getQuestions()) {
      question.getAnswers().forEach(answer -> answer.setQuestion(question));
    }
    return quizRepository.save(quiz);
  }

  @Override
  public Quiz updateQuiz(UpdateQuizRequest updateQuizRequest) {
    LocalUser localUser =
        (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var quiz = modelMapper.map(updateQuizRequest, Quiz.class);
    quiz.setModifiedDate(new Date());
    quiz.setModifiedBy(localUser.getUser().getId());
    quiz.getQuestions()
        .forEach(
            question -> {
              question.setQuiz(quiz);
            });
    for (Question question : quiz.getQuestions()) {
      question.getAnswers().forEach(answer -> answer.setQuestion(question));
    }
    return quizRepository.save(quiz);
  }

  @Override
  public List<QuizTakingHistoryResponse> getQuizTakingHistory() {
    LocalUser localUser =
        (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var userId = localUser.getUser().getId();
    return quizRepository.getQuizTakingHistory(userId).stream()
        .map(
            item -> {
              var quizTakingHistoryResponse =
                  modelMapper.map(item, QuizTakingHistoryResponse.class);
              return quizTakingHistoryResponse;
            })
        .collect(Collectors.toList());
  }

  @Override
  public QuizResultResponse getQuizResult(ResultQuizCheckingRequest request) {
    var results =
        questionRepository.getQuizResult(request.getQuizId()).stream()
            .map(
                ele -> {
                  var item = modelMapper.map(ele, QuizResultCheckingDto.class);
                  return item;
                })
            .collect(Collectors.toList());
    var response = calculateUserResult(request, results);
    saveToQuizResult(response, request.getQuizId());
    return response;
  }

  private void saveToQuizResult(QuizResultResponse response, Long quizId) {
    LocalUser localUser =
        (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var quizResult = modelMapper.map(response, QuizResult.class);
    quizResult.setCreatedDate(new Date());
    quizResult.setQuizId(localUser.getUser().getId());
    quizResult.setUserId(quizId);
    quizResult.setIsDeleted(false);
    quizResultRepository.save(quizResult);
  }

  private QuizResultResponse calculateUserResult(
      ResultQuizCheckingRequest request, List<QuizResultCheckingDto> results) {
    results.sort(
        Comparator.comparing(QuizResultCheckingDto::getQuestionId)
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
      while (results.get(i).getQuestionId() == results.get(i + 1).getQuestionId()
          && i < results.size()) {
        i++;
        answer = answer + results.get(i).getAnswerId() + "-";
      }
      if (i < results.size()) {
        answers.put(questionId, answer);
        i++;
      }
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
        if (answer.isEmpty()) answer = answerId + "-";
        else answer = answer + answerId + "-";
      }
      var trueAnswer = answers.get(questionId);
      if (answer.equals(trueAnswer) == false) numberOfFalse++;
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
  public List<QuizOverviewDto> getListDraftQuiz() {
    LocalUser localUser =
        (LocalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var userId = localUser.getUser().getId();
    return quizRepository.findAllByCreatedByAndIsDraft(userId, true).stream()
        .map(
            quiz -> {
              var quizOverviewDto = modelMapper.map(quiz, QuizOverviewDto.class);
              return quizOverviewDto;
            })
        .collect(Collectors.toList());
  }
}
