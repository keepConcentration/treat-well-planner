package com.world.planner.plan.application;

import com.world.planner.global.util.ValidRecurrenceRuleUtils;
import com.world.planner.member.application.MemberService;
import com.world.planner.member.domain.Member;
import com.world.planner.plan.domain.Category;
import com.world.planner.plan.domain.Plan;
import com.world.planner.plan.domain.recurrence.RecurrenceRuleType;
import com.world.planner.plan.infrastructure.repository.CategoryRepository;
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
  private final CategoryRepository categoryRepository;
  private final MemberService memberService;

  /**
   * 새로운 Plan 생성
   *
   * @param title 제목
   * @param description 설명
   * @param startDate 시작 날짜
   * @param endDate 종료 날짜
   * @param categoryId 카테고리 ID
   * @return 생성된 Plan의 ID
   */
  public UUID createPlan(String title, String description, LocalDate startDate, LocalDate endDate, UUID categoryId) {
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + categoryId));

    Member currentMember = memberService.getAuthenticatedMember();

    // 새로운 Plan 생성 (개선된 create 메서드 사용)
    Plan plan = Plan.create(
        currentMember, // 소유자
        title,
        description,
        startDate,
        endDate,
        category // 카테고리 설정
    );

    // 유효성 검사
    ValidRecurrenceRuleUtils.validateDateRange(plan.getStartDate(), plan.getEndDate());

    // Plan 저장
    planRepository.save(plan);

    // 생성된 Plan의 ID 반환
    return plan.getId();
  }

  // Plan 완료 상태로 변경
  public void markPlanAsCompleted(UUID planId) {
    Plan plan = findPlan(planId); // Plan 엔티티에서 동작 수행
    plan.markAsCompleted();
  }

  // Plan 완료 상태 해제
  public void markPlanAsIncomplete(UUID planId) {
    Plan plan = findPlan(planId); // Plan 엔티티에서 동작 수행
    plan.markAsIncomplete();
  }

  /**
   * "언젠가 할 일"로 표시된 Plan 목록 조회
   *
   * @return PlanDetailResponse의 리스트
   *         - startDate와 endDate가 모두 null인 Plan 목록을 PlanDetailResponse로 변환하여 반환
   */
  @Transactional(readOnly = true)
  public List<PlanDetailResponse> getSomedayPlans() {
    Member currentMember = memberService.getAuthenticatedMember();
    return planRepository.findPlansWithoutDatesByOwner(currentMember).stream()
        .map(PlanDetailResponse::fromEntity)
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
   */
  public void updatePlan(UUID planId, String title, String description, LocalDate startDate, LocalDate endDate) {
    Plan plan = findPlan(planId);
    plan.updateDetails(title, description, startDate, endDate);
  }

  /**
   * 특정 Plan이 주어진 날짜에 활성 상태인지 확인
   *
   * @param planId  Plan ID
   * @param checkDate 확인할 날짜
   * @return Plan의 활성 여부
   */
  @Transactional(readOnly = true)
  public boolean isPlanActive(UUID planId, LocalDate checkDate) {
    Plan plan = findPlan(planId);
    return plan.isActive(checkDate);
  }

  /**
   * Plan 삭제
   *
   * @param planId 삭제하려는 Plan의 ID
   */
  public void deletePlan(UUID planId) {
    Plan plan = findPlan(planId);
    planRepository.delete(plan);
  }

  /**
   * 단일 Plan 조회
   *
   * @param planId 조회할 Plan의 ID
   * @return 조회한 Plan의 상세 Response
   */
  @Transactional(readOnly = true)
  public PlanDetailResponse getPlan(UUID planId) {
    Plan plan = findPlan(planId); // 애그리거트 조회
    return PlanDetailResponse.fromEntity(plan); // DTO로 변환
  }

  /**
   * 전체 Plan 목록 조회
   *
   * @return 전체 Plan 목록의 PlanSummaryResponse 리스트
   */
  @Transactional(readOnly = true)
  public List<PlanSummaryResponse> getAllPlans() {
    Member currentMember = memberService.getAuthenticatedMember();
    return planRepository.findAllByOwner(currentMember).stream()
        .map(PlanSummaryResponse::fromEntity)
        .toList();
  }

  /**
   * Plan에서 RecurrenceRule 삭제
   *
   * @param planId 반복 규칙을 삭제할 대상 Plan의 ID
   */
  public void removeRecurrenceRule(UUID planId) {
    Plan plan = findPlan(planId); // Plan 조회
    plan.removeRecurrenceRule(); // RecurrenceRule 제거
  }

  // 공통 메서드: Plan ID로 애그리거트 조회
  private Plan findPlan(UUID planId) {
    Member currentMember = memberService.getAuthenticatedMember();
    return planRepository.findByIdAndOwner(planId, currentMember)
        .orElseThrow(() -> new EntityNotFoundException("Plan not found with ID: " + planId + " for current user."));
  }

  /**
   * RecurrenceRule 추가 또는 수정 (RecurrenceRuleService를 통해 생성)
   *
   * @param planId Plan ID
   * @param ruleType 반복 규칙 타입 (DAILY, WEEKLY, MONTHLY, YEARLY)
   * @param interval 반복 주기
   * @param daysOfWeek 요일 리스트 (옵션)
   * @param daysOfMonth 날짜 리스트 (옵션)
   * @param monthsOfYear 월 리스트 (연 단위에서 사용, 옵션)
   */
  public void addOrUpdateRecurrenceRule(
      UUID planId,
      RecurrenceRuleType ruleType,
      int interval,
      List<DayOfWeek> daysOfWeek,
      List<Integer> daysOfMonth,
      List<Integer> monthsOfYear
  ) {
    Plan plan = findPlan(planId); // Plan 조회

    // RecurrenceRule 생성
    RecurrenceRule recurrenceRule = recurrenceRuleService.createRecurrenceRule(ruleType, interval, daysOfWeek, daysOfMonth, monthsOfYear);

    // Plan에 RecurrenceRule 추가/수정
    plan.addRecurrenceRule(recurrenceRule);
  }

}
