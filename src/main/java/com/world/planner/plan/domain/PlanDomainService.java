package com.world.planner.plan.domain;

import com.world.planner.plan.domain.recurrence.RecurrenceRule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PlanDomainService {

  /**
   * Plan의 RecurrenceRule을 기반으로 특정 기간의 발생 일자 계산
   *
   * @param plan Plan 객체
   * @param filterStartDate 검색 시작 날짜
   * @param filterEndDate 검색 종료 날짜
   * @return 발생 일자 리스트
   */
  public List<LocalDate> calculateOccurrences(Plan plan, LocalDate filterStartDate, LocalDate filterEndDate) {
    if (plan == null) {
      throw new IllegalArgumentException("Plan 객체는 null일 수 없습니다.");
    }

    RecurrenceRule recurrenceRule = plan.getRecurrenceRule();
    if (!plan.hasRecurrenceRule() || !recurrenceRule.isValid()) {
      throw new IllegalArgumentException("Plan에 유효한 RecurrenceRule이 없습니다.");
    }

    // Plan 적용 기간과 검색 기간 병합
    LocalDate calculationStartDate = plan.getStartDate().isAfter(filterStartDate) ? plan.getStartDate() : filterStartDate;
    LocalDate calculationEndDate = plan.getEndDate().isBefore(filterEndDate) ? plan.getEndDate() : filterEndDate;

    if (calculationStartDate.isAfter(calculationEndDate)) {
      return List.of(); // 병합된 기간이 유효하지 않은 경우 빈 리스트 반환
    }

    // Rule 기반으로 발생 일자 계산
    return calculateOccurrencesBasedOnRule(recurrenceRule, calculationStartDate, calculationEndDate);
  }

  private List<LocalDate> calculateOccurrencesBasedOnRule(RecurrenceRule rule, LocalDate startDate, LocalDate endDate) {
    List<LocalDate> occurrences = new ArrayList<>();
    LocalDate currentDate = startDate;

    while (!currentDate.isAfter(endDate)) {
      if (rule.isOccurrence(currentDate)) {
        occurrences.add(currentDate);
      }
      currentDate = currentDate.plusDays(1);
    }

    return occurrences;
  }

  /**
   * Plan이 특정 날짜에 활성 상태인지 확인
   *
   * @param plan Plan 객체
   * @param checkDate 확인할 날짜
   * @return 활성 여부
   */
  public boolean isPlanActive(Plan plan, LocalDate checkDate) {
    if (plan == null) {
      throw new IllegalArgumentException("Plan 객체는 null일 수 없습니다.");
    }

    // Plan의 startDate - endDate 범위 체크
    if (checkDate.isBefore(plan.getStartDate()) || checkDate.isAfter(plan.getEndDate())) {
      return false;
    }

    RecurrenceRule recurrenceRule = plan.getRecurrenceRule();

    // RecurrenceRule이 없는 경우 고정된 Plan인지 판단
    return recurrenceRule == null || recurrenceRule.isOccurrence(checkDate);
  }
}