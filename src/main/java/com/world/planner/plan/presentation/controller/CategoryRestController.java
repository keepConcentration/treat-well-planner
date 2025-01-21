package com.world.planner.plan.presentation.controller;

import com.world.planner.plan.application.CategoryService;
import com.world.planner.plan.presentation.dto.request.CategoryRequest;
import com.world.planner.plan.presentation.dto.response.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category API", description = "카테고리와 관련된 API를 제공합니다.")
public class CategoryRestController {

  private final CategoryService categoryService;

  @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다.")
  @PostMapping
  public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {
    CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest);
    return ResponseEntity.ok(categoryResponse);
  }

  @Operation(summary = "전체 카테고리 조회", description = "삭제되지 않은 모든 카테고리를 조회합니다.")
  @GetMapping
  public ResponseEntity<List<CategoryResponse>> getAllCategories() {
    List<CategoryResponse> categoryResponses = categoryService.getAllActiveCategories();
    return ResponseEntity.ok(categoryResponses);
  }

  @Operation(summary = "전체 카테고리 조회 (삭제 포함)", description = "삭제된 카테고리를 포함하여 모든 카테고리를 조회합니다.")
  @GetMapping("/all")
  public ResponseEntity<List<CategoryResponse>> getAllCategoriesIncludingDeleted() {
    List<CategoryResponse> categoryResponses = categoryService.getAllCategoriesIncludingDeleted();
    return ResponseEntity.ok(categoryResponses);
  }

  @Operation(summary = "카테고리 삭제", description = "카테고리를 논리적으로 삭제합니다.")
  @DeleteMapping("/{categoryId}")
  public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId) {
    categoryService.deleteCategory(categoryId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "카테고리 조회", description = "ID를 기반으로 단일 카테고리를 조회합니다.")
  @GetMapping("/{categoryId}")
  public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable UUID categoryId) {
    CategoryResponse categoryResponse = categoryService.getCategoryById(categoryId);
    return ResponseEntity.ok(categoryResponse);
  }
}