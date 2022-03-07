package com.labate.mentoringme.repository;

import com.labate.mentoringme.model.quiz.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {}
