package com.world.planner.plan.infrastructure.repository;

import com.world.planner.plan.domain.Plan;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlanRepository extends JpaRepository<Plan, UUID> {

  @Query("SELECT p FROM Plan p WHERE p.startDate IS NULL AND p.endDate IS NULL")
  List<Plan> findPlansWithoutDates();
}
