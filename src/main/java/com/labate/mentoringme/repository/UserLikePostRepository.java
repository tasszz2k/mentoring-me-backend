package com.labate.mentoringme.repository;

import com.labate.mentoringme.model.UserLikePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserLikePostRepository extends JpaRepository<UserLikePost, Long> {
  UserLikePost findByKey(UserLikePost.Key key);

  Set<UserLikePost> findAllByKeyPostIdAndIsDeletedIsFalse(Long id);
}
