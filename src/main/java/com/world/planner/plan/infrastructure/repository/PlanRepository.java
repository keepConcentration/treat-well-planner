package com.world.planner.plan.infrastructure.repository;

import com.world.planner.member.domain.Member;
import com.world.planner.plan.domain.Plan;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlanRepository extends JpaRepository<Plan, UUID> {

  @Query("SELECT p FROM Plan p WHERE p.startDate IS NULL AND p.endDate IS NULL AND p.owner = :owner")
  List<Plan> findPlansWithoutDatesByOwner(Member owner);

  List<Plan> findAllByOwner(Member member);

  Optional<Plan> findByIdAndOwner(UUID planId, Member owner);
}
