package com.labate.mentoringme.service.category;

import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.request.GetCategoriesRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.model.Category;
import com.labate.mentoringme.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;

  @Override
  public Category findById(Long id) {
    if (id == null) {
      return null;
    }
    return categoryRepository.findById(id).orElse(null);
  }

  @Override
  public Page<Category> findAllCategories(PageCriteria pageCriteria, GetCategoriesRequest request) {
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    return categoryRepository.findAllByConditions(request, pageable);
  }

  @Override
  public Category saveCategory(Category category) {
    return categoryRepository.save(category);
  }

  @Override
  public void deleteCategory(Long id) {}

  @Override
  public List<Category> findByIds(List<Long> categoryIds) {
    return categoryRepository.findAllById(categoryIds);
  }
}
