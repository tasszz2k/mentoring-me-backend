package com.labate.mentoringme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.labate.mentoringme.model.UserProfile;

@Repository
public interface ProfileRepository extends JpaRepository<UserProfile, Long> {
  @Modifying
  @Query("UPDATE UserProfile u SET u.rating = :rating WHERE u.id = :id")
  void updateRating(@Param("rating") Float rating, @Param("id") Long id);
}
