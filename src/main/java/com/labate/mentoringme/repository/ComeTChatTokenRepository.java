package com.labate.mentoringme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.labate.mentoringme.model.ComeTChatToken;

@Repository
public interface ComeTChatTokenRepository extends JpaRepository<ComeTChatToken, Long> {

  ComeTChatToken findByUserId(@Param("userId") Long userId);
}
