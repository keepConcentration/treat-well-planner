package com.world.planner.plan.domain;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Plan> plans = new HashSet<>();

    private Tag(String name) {
        this.name = name;
    }

    public static Tag create(String name) {
        validateName(name);
        return new Tag(name);
    }

    public void addPlan(Plan plan) {
        if (plan != null) {
            plans.add(plan);
            plan.getTags().add(this); // 양방향 관계 설정
        }
    }

    public void removePlan(Plan plan) {
        if (plan != null) {
            plans.remove(plan);
            plan.getTags().remove(this); // 양방향 관계 제거
        }
    }

    // 태그 이름 유효성 검사
    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tag name cannot be null or empty.");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("Tag name cannot exceed 50 characters.");
        }
    }
}
