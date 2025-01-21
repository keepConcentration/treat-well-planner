package com.world.planner.plan.infrastructure.repository;

import com.world.planner.plan.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
  List<Category> findByDeletedFalse(); // 삭제되지 않은 카테고리 조회
}
