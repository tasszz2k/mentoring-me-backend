package com.labate.mentoringme.repository;

import com.labate.mentoringme.model.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    FcmToken findByUserIdAndToken(Long userId, String tokenId);

    List<FcmToken> findByUserIdIn(Collection<Long> userIds);
}
