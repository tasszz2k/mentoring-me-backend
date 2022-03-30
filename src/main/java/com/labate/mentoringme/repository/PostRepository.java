package com.labate.mentoringme.repository;

import com.labate.mentoringme.dto.request.GetPostsRequest;
import com.labate.mentoringme.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
  @Query(
      "SELECT p FROM Post p WHERE "
          + " (:#{#request.status} is null or p.status = :#{#request.status}) "
          + "and (:#{#request.createdBy} is null or p.createdBy = :#{#request.createdBy})"
          + "AND (COALESCE(:#{#request.categoryIds}, NULL) IS NULL OR p.category.id IN (:#{#request.categoryIds})) "
          + "and (:#{#request.minPrice} is null or p.price >= :#{#request.minPrice}) "
          + "and (:#{#request.maxPrice} is null or p.price <= :#{#request.maxPrice}) "
          + "and (:#{#request.fromDate} is null or p.startDate >= :#{#request.fromDate}) "
          + "and (:#{#request.toDate} is null or p.startDate <= :#{#request.toDate}) "
          + "and (:#{#request.keyword} is null "
          + "or p.content like %:#{#request.keyword}% "
          + ") ")
  Page<Post> findAllByConditions(GetPostsRequest request, Pageable pageable);
}
