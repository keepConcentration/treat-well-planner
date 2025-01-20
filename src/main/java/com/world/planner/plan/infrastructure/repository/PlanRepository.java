package com.world.planner.plan.infrastructure.repository;

import com.world.planner.plan.domain.Plan;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, UUID> {

}
