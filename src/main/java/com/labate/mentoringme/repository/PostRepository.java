package com.labate.mentoringme.repository;

import com.labate.mentoringme.dto.request.GetPostsRequest;
import com.labate.mentoringme.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
  @Query("select p from Post p")
  Page<Post> findAllByConditions(GetPostsRequest request, Pageable pageable);
}
