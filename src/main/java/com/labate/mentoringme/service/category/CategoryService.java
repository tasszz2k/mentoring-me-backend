package com.labate.mentoringme.service.category;

import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.model.Category;
import org.springframework.data.domain.Page;

public interface CategoryService {
    Category findById(Long id);
    Page<Category> findAllCategories(PageCriteria pageCriteria);
    Category saveCategory(Category category);
    void deleteCategory(Long id);
}
