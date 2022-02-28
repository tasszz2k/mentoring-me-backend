package com.labate.mentoringme.service.category;

import com.labate.mentoringme.dto.request.GetCategoriesRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.model.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    Category findById(Long id);
    Page<Category> findAllCategories(PageCriteria pageCriteria, GetCategoriesRequest request);
    Category saveCategory(Category category);
    void deleteCategory(Long id);

    List<Category> findByIds(List<Long> categoryIds);
}
