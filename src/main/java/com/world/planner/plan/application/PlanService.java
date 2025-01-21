package com.world.planner.plan.application;

import com.world.planner.global.util.ValidRecurrenceRuleUtils;
import com.world.planner.plan.domain.Plan;
import com.world.planner.plan.domain.PlanDomainService;
import com.world.planner.plan.domain.recurrence.RecurrenceRuleType;
import com.world.planner.plan.infrastructure.repository.PlanRepository;
import com.world.planner.plan.domain.recurrence.RecurrenceRule;
import com.world.planner.plan.presentation.dto.response.PlanDetailResponse;
import com.world.planner.plan.presentation.dto.response.PlanSummaryResponse;
import jakarta.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanService {

  private final PlanRepository planRepository;
  private final RecurrenceRuleService recurrenceRuleService;
  private final PlanDomainService planDomainService;

  /**
   * 새로운 Plan 생성
   *
   * @param title       Plan 제목
   * @param description Plan 설명
   * @param startDate   시작 날짜
   * @param endDate     종료 날짜
   * @return 생성된 Plan의 ID
   */
  public UUID createPlan(String title, String description, LocalDate startDate, LocalDate endDate) {
    Plan plan = Plan.create(title, description, startDate, endDate);
    // 유효성 검사
    if (!ValidRecurrenceRuleUtils.isValidDateRange(plan.getStartDate(), plan.getEndDate())) {
      throw new IllegalArgumentException("Invalid date range");
    }

    planRepository.save(plan);
    return plan.getId();
  }

  /**
   * "언젠가 할 일"로 표시된 Plan 목록 조회
   *
   * @return PlanDetailResponse의 리스트
   *         - startDate와 endDate가 모두 null인 Plan 목록을 PlanDetailResponse로 변환하여 반환
   */

  public List<PlanDetailResponse> getSomedayPlans() {
    List<Plan> plans = planRepository.findPlansWithoutDates();
    return plans.stream()
            .map(PlanDetailResponse::fromEntity) // DTO로 변환
            .collect(Collectors.toList());
  }

  /**
   * 기존 Plan 수정
   *
   * @param planId      수정할 Plan의 ID
   * @param title       수정된 Plan 제목
   * @param description 수정된 Plan 설명
   * @param startDate   수정된 시작 날짜
   * @param endDate     수정된 종료 날짜
   * @return 수정된 PlanDetailResponse 객체
   */
  public PlanDetailResponse updatePlan(UUID planId, String title, String description, LocalDate startDate, LocalDate endDate) {
    Plan plan = findPlanById(planId);
    plan.updateDetails(title, description, startDate, endDate);
    return PlanDetailResponse.fromEntity(plan);
  }

  /**
   * 특정 Plan이 주어진 날짜에 활성 상태인지 확인
   *
   * @param planId  Plan ID
   * @param checkDate 확인할 날짜
   * @return 활성 여부
   */
  @Transactional(readOnly = true)
  public boolean isPlanActive(UUID planId, LocalDate checkDate) {
    Plan plan = findPlanById(planId);
    return planDomainService.isPlanActive(plan, checkDate);
  }

  /**
   * Plan 삭제
   *
   * @param planId 삭제하려는 Plan의 ID
   */
  public void deletePlan(UUID planId) {
    Plan plan = findPlanById(planId);
    planRepository.delete(plan); // 애그리거트 삭제
  }

  /**
   * 단일 Plan 조회
   *
   * @param planId 조회할 Plan의 ID
   * @return PlanDetailResponse
   */
  @Transactional(readOnly = true)
  public PlanDetailResponse getPlan(UUID planId) {
    Plan plan = findPlanById(planId); // 애그리거트 조회
    return PlanDetailResponse.fromEntity(plan); // DTO로 변환
  }

  /**
   * 전체 Plan 목록 조회
   *
   * @return Plan 목록의 PlanSummaryResponse 리스트
   */
  @Transactional(readOnly = true)
  public List<PlanSummaryResponse> getAllPlans() {
    return planRepository.findAll().stream()
        .map(PlanSummaryResponse::fromEntity) // Simple DTO로 변환
        .toList();
  }

  /**
   * RecurrenceRule 추가 또는 수정 (RecurrenceRuleService를 통해 생성)
   *
   * @param planId      Plan ID
   * @param ruleType    반복 규칙 타입 (DAILY, WEEKLY, MONTHLY, YEARLY)
   * @param interval    반복 주기
   * @param daysOfWeek  (옵션) 요일 리스트
   * @param daysOfMonth (옵션) 날짜 리스트
   * @param monthsOfYear (옵션) 월 리스트 (연 단위에서 사용)
   * @return 수정된 PlanDetailResponse
   */
  public PlanDetailResponse addOrUpdateRecurrenceRule(
      UUID planId,
      RecurrenceRuleType ruleType,
      int interval,
      List<DayOfWeek> daysOfWeek,
      List<Integer> daysOfMonth,
      List<Integer> monthsOfYear
  ) {
    Plan plan = findPlanById(planId); // Plan 조회

    // RecurrenceRule 생성 (RecurrenceRuleService 사용)
    RecurrenceRule recurrenceRule = createRecurrenceRule(ruleType, interval, daysOfWeek, daysOfMonth, monthsOfYear);

    // Plan에 RecurrenceRule 추가/수정
    plan.addRecurrenceRule(recurrenceRule);
    return PlanDetailResponse.fromEntity(plan); // 수정된 Plan 반환
  }

  /**
   * Plan에서 RecurrenceRule 삭제
   *
   * @param planId Plan ID
   * @return 수정된 PlanDetailResponse
   */
  public PlanDetailResponse removeRecurrenceRule(UUID planId) {
    Plan plan = findPlanById(planId); // Plan 조회
    plan.removeRecurrenceRule(); // RecurrenceRule 제거
    return PlanDetailResponse.fromEntity(plan);
  }

  // 공통 메서드: Plan ID로 애그리거트 조회
  private Plan findPlanById(UUID planId) {
    return planRepository.findById(planId)
        .orElseThrow(() -> new EntityNotFoundException("Plan not found with ID: " + planId));
  }

  // RecurrenceRule 생성 로직 (RecurrenceRuleService 의존)
  private RecurrenceRule createRecurrenceRule(
      RecurrenceRuleType ruleType,
      int interval,
      List<DayOfWeek> daysOfWeek,
      List<Integer> daysOfMonth,
      List<Integer> monthsOfYear
  ) {
    return switch (ruleType) {
      case DAILY -> recurrenceRuleService.createDailyRule(interval);
      case WEEKLY -> recurrenceRuleService.createWeeklyRule(interval, daysOfWeek);
      case MONTHLY -> recurrenceRuleService.createMonthlyRule(interval, daysOfWeek, daysOfMonth);
      case YEARLY -> recurrenceRuleService.createYearlyRule(interval, monthsOfYear, daysOfWeek, daysOfMonth);
    };
  }

}
