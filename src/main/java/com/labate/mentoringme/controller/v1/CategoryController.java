package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.dto.mapper.CategoryMapper;
import com.labate.mentoringme.dto.model.CategoryDto;
import com.labate.mentoringme.dto.request.GetCategoriesRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.response.ApiResponse;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.dto.response.Metadata;
import com.labate.mentoringme.service.category.CategoryService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
  private final CategoryService categoryService;

  @GetMapping("/{categoryId}")
  public ResponseEntity<?> findCategoryById(@PathVariable Long categoryId) {
    var category = categoryService.findById(categoryId);
    if (category == null) {
      return ResponseEntity.badRequest().body(ApiResponse.fail(null, "Category not found"));
    }
    return BaseResponseEntity.ok(CategoryMapper.toDto(category));
  }

  @GetMapping("")
  public ResponseEntity<?> findAllCategories(
      @Valid PageCriteria pageCriteria, @Valid GetCategoriesRequest request) {
    var page = categoryService.findAllCategories(pageCriteria, request);
    var categories = page.getContent();

    var metadata =
        Metadata.builder()
            .limit(pageCriteria.getLimit())
            .page(pageCriteria.getPage())
            .total(page.getTotalElements())
            .build();

    return BaseResponseEntity.ok(CategoryMapper.toDtos(categories), metadata);
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  @PostMapping("")
  public ResponseEntity<?> addNewCategory(@Valid @RequestBody CategoryDto categoryDto) {

    var category = CategoryMapper.toEntity(categoryDto);
    category.setId(null);
    category = categoryService.saveCategory(category);

    return BaseResponseEntity.ok(CategoryMapper.toDto(category));
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  @PutMapping("/{categoryId}")
  public ResponseEntity<?> updateCategory(
      @PathVariable Long categoryId, @Valid @RequestBody CategoryDto categoryDto) {

    var oldCategory = categoryService.findById(categoryId);
    if (oldCategory == null) {
      return ResponseEntity.badRequest().body(ApiResponse.fail(null, "Category not found"));
    }

    categoryDto.setId(categoryId);
    var category = CategoryMapper.toEntity(categoryDto);
    category = categoryService.saveCategory(category);

    return BaseResponseEntity.ok(CategoryMapper.toDto(category));
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  @DeleteMapping("/{categoryId}")
  public ResponseEntity<?> updateCategory(@PathVariable Long categoryId) {
    var oldCategory = categoryService.findById(categoryId);
    if (oldCategory == null) {
      return ResponseEntity.badRequest().body(ApiResponse.fail(null, "Category not found"));
    }

    categoryService.deleteCategory(categoryId);

    return BaseResponseEntity.ok(null, "Category deleted successfully");
  }
}
