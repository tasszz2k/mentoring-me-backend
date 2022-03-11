package com.labate.mentoringme.repository;

import com.labate.mentoringme.model.UserLikePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLikePostRepository extends JpaRepository<UserLikePost, Long> {
  UserLikePost findByKey(UserLikePost.Key key);
}
