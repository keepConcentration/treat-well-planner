package com.world.planner.plan.presentation.controller;

import com.world.planner.plan.application.PlanService;
import com.world.planner.plan.presentation.dto.response.PlanDetailResponse;
import com.world.planner.plan.presentation.dto.response.PlanSummaryResponse;
import com.world.planner.plan.presentation.dto.request.CreatePlanRequest;
import com.world.planner.plan.presentation.dto.request.RecurrenceRuleRequest;
import com.world.planner.plan.presentation.dto.request.UpdatePlanRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "/api/plans", produces = "application/json; charset=UTF-8")
@RequiredArgsConstructor
@Tag(name = "Plan API", description = "Plan 관련 API")
public class PlanRestController {

  private final PlanService planService;

  /**
   * Plan 생성
   *
   * @param request Plan 생성 요청
   * @return 생성된 Plan ID의 응답
   */
  @PostMapping
  @Operation(summary = "Plan 생성", description = "새로운 Plan을 추가합니다.")
  public ResponseEntity<Void> createPlan(@Valid @RequestBody CreatePlanRequest request) {
    UUID planId = planService.createPlan(
        request.getTitle(),
        request.getDescription(),
        request.getStartDate(),
        request.getEndDate() // 변경된 필드 적용
    );

    // Location 헤더를 설정하여 생성된 자원의 URI를 반환
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .header("Location", "/api/plans/" + planId) // 생성된 Plan의 접근 경로
        .build();         // 생성된 ID를 포함한 응답 바디
  }

  /**
   * Plan 수정
   *
   * @param planId  수정할 Plan의 ID
   * @param request Plan 수정 요청
   * @return 수정된 PlanDetailResponse
   */
  @PutMapping("/{planId}")
  @Operation(summary = "Plan 수정", description = "특정 Plan의 내용을 수정합니다.")
  public ResponseEntity<PlanDetailResponse> updatePlan(
      @PathVariable UUID planId,
      @Valid @RequestBody UpdatePlanRequest request
  ) {
    PlanDetailResponse updatedPlan = planService.updatePlan(
        planId,
        request.getTitle(),
        request.getDescription(),
        request.getStartDate(), // 변경된 필드 적용
        request.getEndDate()    // 변경된 필드 적용
    );
    return ResponseEntity.ok(updatedPlan);
  }

  /**
   * Plan 삭제
   *
   * @param planId 삭제할 Plan의 ID
   */
  @DeleteMapping("/{planId}")
  @Operation(summary = "Plan 삭제", description = "특정 Plan을 삭제합니다.")
  public ResponseEntity<Void> deletePlan(@PathVariable UUID planId) {
    planService.deletePlan(planId);
    return ResponseEntity.noContent().build();
  }

  /**
   * 단일 Plan 조회
   *
   * @param planId 조회할 Plan의 ID
   * @return PlanDetailResponse
   */
  @GetMapping("/{planId}")
  @Operation(summary = "Plan 조회", description = "특정 Plan의 상세 정보를 가져옵니다.")
  public ResponseEntity<PlanDetailResponse> getPlan(@PathVariable UUID planId) {
    PlanDetailResponse plan = planService.getPlan(planId);
    return ResponseEntity.ok(plan);
  }

  /**
   * 모든 Plan 목록 조회
   *
   * @return 모든 PlanSummaryResponse 리스트
   */
  @GetMapping
  @Operation(summary = "모든 Plan 조회", description = "전체 Plan 목록을 가져옵니다.")
  public ResponseEntity<List<PlanSummaryResponse>> getAllPlans() {
    List<PlanSummaryResponse> plans = planService.getAllPlans();
    return ResponseEntity.ok(plans);
  }

  /**
   * Plan에 RecurrenceRule 추가 또는 수정
   *
   * @param planId      Plan ID
   * @param ruleRequest RecurrenceRule 생성 요청
   * @return 수정된 PlanDetailResponse
   */
  @PostMapping("/{planId}/recurrence-rule")
  @Operation(summary = "반복 규칙 추가 또는 수정", description = "특정 Plan에 반복 규칙을 추가 또는 수정합니다.")
  public ResponseEntity<PlanDetailResponse> addOrUpdateRecurrenceRule(
      @PathVariable UUID planId,
      @Valid @RequestBody RecurrenceRuleRequest ruleRequest
  ) {
    PlanDetailResponse updatedPlan = planService.addOrUpdateRecurrenceRule(
        planId,
        ruleRequest.getRuleType(),
        ruleRequest.getInterval(),
        ruleRequest.getDaysOfWeek(),
        ruleRequest.getDaysOfMonth(),
        ruleRequest.getMonthsOfYear()
    );
    return ResponseEntity.ok(updatedPlan);
  }

  /**
   * Plan에서 RecurrenceRule 삭제
   *
   * @param planId Plan ID
   * @return 수정된 PlanDetailResponse
   */
  @DeleteMapping("/{planId}/recurrence-rule")
  @Operation(summary = "반복 규칙 삭제", description = "특정 Plan의 반복 규칙을 삭제합니다.")
  public ResponseEntity<PlanDetailResponse> removeRecurrenceRule(@PathVariable UUID planId) {
    PlanDetailResponse updatedPlan = planService.removeRecurrenceRule(planId);
    return ResponseEntity.ok(updatedPlan);
  }
}