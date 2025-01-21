package com.world.planner.plan.domain;

import com.world.planner.global.event.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true, length = 50)
  private String name;

  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
  private Set<Plan> plans = new HashSet<>();

  @Column(nullable = false)
  private boolean deleted = false;

  private Category(String name) {
    this.name = name;
  }

  public static Category create(String name) {
    validateName(name);
    return new Category(name);
  }

  public void delete() {
    this.deleted = true; // 삭제 상태를 true로 설정
  }

  public void restore() {
    this.deleted = false; // 삭제 상태를 복원
  }

  private static void validateName(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Category name cannot be null or empty.");
    }
    if (name.length() > 50) {
      throw new IllegalArgumentException("Category name cannot exceed 50 characters.");
    }
  }
}