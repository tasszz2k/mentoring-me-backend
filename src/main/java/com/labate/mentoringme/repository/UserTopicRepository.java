package com.labate.mentoringme.repository;

import com.labate.mentoringme.model.UserTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTopicRepository extends JpaRepository<UserTopic, Long> {}
