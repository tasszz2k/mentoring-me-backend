package com.labate.mentoringme.service.category;

import com.labate.mentoringme.dto.request.GetCategoriesRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.model.Category;
import com.labate.mentoringme.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

  @InjectMocks CategoryServiceImpl categoryService;
  @Mock CategoryRepository categoryRepository;

  static List<Category> categories;

  @BeforeAll
  static void beforeAll() {
    // Initialize the 10 categories data
    categories =
        List.of(
            Category.builder().id(1L).parentCategoryId(null).name("Toán").code("math").build(),
            Category.builder()
                .id(2L)
                .parentCategoryId(1L)
                .name("Toán tiểu học")
                .code("math-1")
                .build(),
            Category.builder()
                .id(3L)
                .parentCategoryId(1L)
                .name("Toán Trung học cơ sở")
                .code("math-2")
                .build(),
            Category.builder()
                .id(4L)
                .parentCategoryId(1L)
                .name("Toán Trung học phổ thông")
                .code("math-3")
                .build(),
            Category.builder()
                .id(5L)
                .parentCategoryId(1L)
                .name("Toán ôn thi Đại học")
                .code("math-4")
                .build(),
            Category.builder()
                .id(6L)
                .parentCategoryId(1L)
                .name("Toán Đại học")
                .code("math-5")
                .build(),
            Category.builder()
                .id(7L)
                .parentCategoryId(1L)
                .name("Toán Logic học bổng FPT")
                .code("math-6")
                .build(),
            Category.builder()
                .id(8L)
                .parentCategoryId(1L)
                .name("Toán luyện thi chuyên")
                .code("math-7")
                .build(),
            Category.builder()
                .id(9L)
                .parentCategoryId(1L)
                .name("Toán luyện thi HSG")
                .code("math-8")
                .build(),
            Category.builder()
                .id(10L)
                .parentCategoryId(null)
                .name("Tiếng Anh")
                .code("english")
                .build(),
            Category.builder()
                .id(11L)
                .parentCategoryId(10L)
                .name("Tiếng Anh tiểu học")
                .code("english-1")
                .build());
  }

  @Test
  void testFindById_T01() {
    when(categoryRepository.findById(2L)).thenReturn(Optional.ofNullable(categories.get(1)));
    var result = categoryService.findById(2L);
    Assertions.assertEquals(categories.get(1), result);
  }

  @Test
  void testFindById_T02() {
    when(categoryRepository.findById(3L)).thenReturn(Optional.ofNullable(categories.get(2)));
    var result = categoryService.findById(3L);
    Assertions.assertEquals(categories.get(2), result);
  }

  @Test
  void testFindById_T03() {
    var result = categoryService.findById(null);
    Assertions.assertNull(result);
  }

  @Test
  void testFindById_T04() {
    when(categoryRepository.findById(-1L)).thenReturn(Optional.empty());
    var result = categoryService.findById(-1L);
    Assertions.assertNull(result);
  }

  @Test
  void testFindAllCategories_01() {
    when(categoryRepository.findAll()).thenReturn(categories);
    var result = categoryService.findAllCategories();
    Assertions.assertEquals(11, result.size());
  }

  @Test
  void testFindAllCategories_02() {
    var pageCriteria = PageCriteria.builder().page(1).limit(5).build();
    var request = new GetCategoriesRequest(null, null);
    Page<Category> pageData = new PageImpl<>(categories.subList(0, 5), PageRequest.of(0, 5), 11);
    when(categoryService.findAllCategories(pageCriteria, request)).thenReturn(pageData);
    var result = categoryService.findAllCategories(pageCriteria, request);
    Assertions.assertEquals(pageData, result);
  }

  @Test
  void testFindAllCategories_03() {
    var pageCriteria = PageCriteria.builder().build();
    var request = new GetCategoriesRequest(null, null);
    Page<Category> pageData = new PageImpl<>(categories, PageRequest.of(0, Integer.MAX_VALUE), 11);
    when(categoryService.findAllCategories(pageCriteria, request)).thenReturn(pageData);
    var result = categoryService.findAllCategories(pageCriteria, request);
    Assertions.assertEquals(pageData, result);
  }
}
