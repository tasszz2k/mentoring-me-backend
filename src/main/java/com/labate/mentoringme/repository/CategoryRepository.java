package com.labate.mentoringme.repository;

import com.labate.mentoringme.dto.request.GetCategoriesRequest;
import com.labate.mentoringme.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  Page<Category> findAll(Pageable pageable);

  @Query(
      value =
          "SELECT *\n"
              + "FROM categories\n"
              + "WHERE (:#{#request.parentCategoryId} IS NULL OR parent_category_id = :#{#request.parentCategoryId})\n"
              + "AND (:#{#request.isParent} IS NULL OR\n"
              + " CASE WHEN :#{#request.isParent} THEN parent_category_id IS NULL ELSE  parent_category_id IS NOT NULL END) ",
      nativeQuery = true)
  Page<Category> findAllByConditions(GetCategoriesRequest request, Pageable pageable);
}
