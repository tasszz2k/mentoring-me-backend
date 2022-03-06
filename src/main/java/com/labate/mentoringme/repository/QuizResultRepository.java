package com.labate.mentoringme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.labate.mentoringme.model.quiz.QuizResult;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {

}
