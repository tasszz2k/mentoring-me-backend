package com.labate.mentoringme.service.quizz;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.model.QuizDto;
import com.labate.mentoringme.dto.model.QuizOverviewDto;
import com.labate.mentoringme.dto.request.FindQuizRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.quiz.CreateQuizRequest;
import com.labate.mentoringme.dto.request.quiz.UpdateQuizRequest;
import com.labate.mentoringme.dto.response.ViewQuizTakingHistoryResponse;
import com.labate.mentoringme.model.quiz.Question;
import com.labate.mentoringme.model.quiz.Quiz;
import com.labate.mentoringme.repository.QuizRepository;
import com.labate.mentoringme.util.ObjectMapperUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class QuizServiceImpl implements QuizService {

	private final QuizRepository quizRepository;

	private final ModelMapper modelMapper = new ModelMapper();

	@Override
	public Page<QuizDto> findAllQuiz(FindQuizRequest request, PageCriteria pageCriteria) {
		var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
		var response = quizRepository.findAllByConditions(request, pageable).map(quiz -> {
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
	public List<QuizOverviewDto> getDraftQuizByUserId(Long userId) {
		return quizRepository.findAllByCreatedByAndIsDraft(userId, true).stream().map(quiz -> {
			var quizOverviewDto = modelMapper.map(quiz, QuizOverviewDto.class);
			return quizOverviewDto;
		}).collect(Collectors.toList());
	}

	@Override
	public Quiz addQuiz(CreateQuizRequest createQuizRequest) {
		var quiz = modelMapper.map(createQuizRequest, Quiz.class);
		quiz.setCreatedDate(new Date());
		quiz.setModifiedDate(new Date());
		quiz.getQuestions().forEach(question -> {
			question.setQuiz(quiz);
		});
		for (Question question : quiz.getQuestions()) {
			question.getAnswers().forEach(answer -> answer.setQuestion(question));
		}
		return quizRepository.save(quiz);
	}

	@Override
	public Quiz updateQuiz(UpdateQuizRequest updateQuizRequest) {
		var quiz = modelMapper.map(updateQuizRequest, Quiz.class);
		quiz.setModifiedDate(new Date());
		quiz.getQuestions().forEach(question -> {
			question.setQuiz(quiz);
		});
		for (Question question : quiz.getQuestions()) {
			question.getAnswers().forEach(answer -> answer.setQuestion(question));
		}
		return quizRepository.save(quiz);
	}

	@Override
	public void saveDraftQuiz(Long quizId) {
		quizRepository.saveDraftQuiz(quizId);
	}

//	@Override
//	public List<ViewQuizTakingHistoryResponse> getQuizTakingHistory(Long userId) {
//		var response = quizRepository.getQuizTakingHistory(userId).stream().map(ele -> {
//			var item = modelMapper.map(ele, ViewQuizTakingHistoryResponse.class);
//			return item;
//		}).collect(Collectors.toList());
//		return response;
//	}
}
