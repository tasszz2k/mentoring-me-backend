package com.labate.mentoringme.repository;

import com.labate.mentoringme.model.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    FcmToken findByUserIdAndToken(Long userId, String tokenId);
}
