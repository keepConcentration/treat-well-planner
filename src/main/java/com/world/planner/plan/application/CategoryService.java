package com.world.planner.plan.application;

import com.world.planner.plan.presentation.dto.request.CategoryRequest;
import com.world.planner.plan.presentation.dto.response.CategoryResponse;
import com.world.planner.plan.domain.Category;
import com.world.planner.plan.infrastructure.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  // 카테고리 생성
  @Transactional
  public CategoryResponse createCategory(CategoryRequest categoryRequest) {
    Category category = Category.create(categoryRequest.getName());
    categoryRepository.save(category);

    return CategoryResponse.builder()
        .id(category.getId())
        .name(category.getName())
        .deleted(category.isDeleted())
        .build();
  }

  // 전체 카테고리 조회 (삭제되지 않은 것만)
  @Transactional(readOnly = true)
  public List<CategoryResponse> getAllActiveCategories() {
    return categoryRepository.findByDeletedFalse().stream()
        .map(category -> CategoryResponse.builder()
            .id(category.getId())
            .name(category.getName())
            .deleted(category.isDeleted())
            .build())
        .collect(Collectors.toList());
  }

  // 삭제된 카테고리 포함한 전체 조회
  @Transactional(readOnly = true)
  public List<CategoryResponse> getAllCategoriesIncludingDeleted() {
    return categoryRepository.findAll().stream()
        .map(category -> CategoryResponse.builder()
            .id(category.getId())
            .name(category.getName())
            .deleted(category.isDeleted())
            .build())
        .collect(Collectors.toList());
  }

  // 카테고리 논리 삭제
  @Transactional
  public void deleteCategory(UUID categoryId) {
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    category.delete(); // 논리적 삭제 처리
  }

  // 단일 카테고리 조회
  @Transactional(readOnly = true)
  public CategoryResponse getCategoryById(UUID categoryId) {
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("Category not found"));

    return CategoryResponse.builder()
        .id(category.getId())
        .name(category.getName())
        .deleted(category.isDeleted())
        .build();
  }
}
