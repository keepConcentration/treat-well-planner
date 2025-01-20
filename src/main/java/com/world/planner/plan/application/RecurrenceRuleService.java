package com.world.planner.plan.application;

import com.world.planner.plan.domain.recurrence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecurrenceRuleService {

  /**
   * DailyRule 생성
   *
   * @param interval 반복 주기
   * @return 생성된 DailyRule
   */
  public DailyRule createDailyRule(int interval) {
    validateInterval(interval);
    return (DailyRule) RecurrenceRuleFactory.createDailyRule(interval);
  }

  /**
   * WeeklyRule 생성
   *
   * @param interval   반복 주기
   * @param daysOfWeek 반복 요일 리스트
   * @return 생성된 WeeklyRule
   */
  public WeeklyRule createWeeklyRule(int interval, List<DayOfWeek> daysOfWeek) {
    validateInterval(interval);
    validateDaysOfWeek(daysOfWeek);
    return (WeeklyRule) RecurrenceRuleFactory.createWeeklyRule(interval, daysOfWeek);
  }

  /**
   * MonthlyRule 생성
   *
   * @param interval     반복 주기
   * @param daysOfWeek   매달 반복되는 요일 리스트 (선택적)
   * @param daysOfMonth  매달 반복되는 날짜 리스트 (선택적)
   * @return 생성된 MonthlyRule
   */
  public MonthlyRule createMonthlyRule(int interval, List<DayOfWeek> daysOfWeek, List<Integer> daysOfMonth) {
    validateInterval(interval);
    validateDaysOfWeekOrDaysOfMonth(daysOfWeek, daysOfMonth);
    return (MonthlyRule) RecurrenceRuleFactory.createMonthlyRule(interval, daysOfWeek, daysOfMonth);
  }

  /**
   * YearlyRule 생성
   *
   * @param interval     반복 주기
   * @param monthsOfYear 매년에 반복되는 월 리스트 (선택적)
   * @param daysOfWeek   매년에 반복되는 요일 리스트 (선택적)
   * @param daysOfMonth  매년에 반복되는 날짜 리스트 (선택적)
   * @return 생성된 YearlyRule
   */
  public YearlyRule createYearlyRule(
      int interval,
      List<Integer> monthsOfYear,
      List<DayOfWeek> daysOfWeek,
      List<Integer> daysOfMonth
  ) {
    validateInterval(interval);
    validateMonthsOfYear(monthsOfYear);
    validateDaysOfWeekOrDaysOfMonth(daysOfWeek, daysOfMonth);
    return (YearlyRule) RecurrenceRuleFactory.createYearlyRule(interval, monthsOfYear, daysOfWeek, daysOfMonth);
  }

  /**
   * 공통: 주기 검증
   *
   * @param interval 반복 주기
   */
  private void validateInterval(int interval) {
    if (interval <= 0) {
      throw new IllegalArgumentException("Interval must be greater than 0.");
    }
  }

  /**
   * WeeklyRule 요일 리스트 검증
   *
   * @param daysOfWeek 요일 리스트
   */
  private void validateDaysOfWeek(List<DayOfWeek> daysOfWeek) {
    if (daysOfWeek == null || daysOfWeek.isEmpty()) {
      throw new IllegalArgumentException("At least one day of the week must be specified.");
    }
  }

  /**
   * MonthlyRule, YearlyRule 날짜/요일 리스트 검증
   *
   * @param daysOfWeek  요일 리스트 (선택적)
   * @param daysOfMonth 날짜 리스트 (선택적)
   */
  private void validateDaysOfWeekOrDaysOfMonth(List<DayOfWeek> daysOfWeek, List<Integer> daysOfMonth) {
    if ((daysOfWeek == null || daysOfWeek.isEmpty()) && (daysOfMonth == null || daysOfMonth.isEmpty())) {
      throw new IllegalArgumentException("Either days of the week or days of the month must be specified.");
    }
    if (daysOfWeek != null && !daysOfWeek.isEmpty()) {
      validateDaysOfWeek(daysOfWeek);
    }
    if (daysOfMonth != null && !daysOfMonth.isEmpty()) {
      validateDaysOfMonth(daysOfMonth);
    }
  }

  /**
   * MonthlyRule, YearlyRule 날짜 리스트 검증
   *
   * @param daysOfMonth 날짜 리스트
   */
  private void validateDaysOfMonth(List<Integer> daysOfMonth) {
    if (daysOfMonth == null || daysOfMonth.isEmpty()) {
      throw new IllegalArgumentException("At least one day of the month must be specified.");
    }
    if (daysOfMonth.stream().anyMatch(day -> day <= 0 || day > 31)) {
      throw new IllegalArgumentException("Days of the month must be between 1 and 31.");
    }
  }

  /**
   * YearlyRule 월 리스트 검증
   *
   * @param monthsOfYear 월 리스트
   */
  private void validateMonthsOfYear(List<Integer> monthsOfYear) {
    if (monthsOfYear != null && !monthsOfYear.isEmpty()) {
      if (monthsOfYear.stream().anyMatch(month -> month <= 0 || month > 12)) {
        throw new IllegalArgumentException("Months of the year must be between 1 and 12.");
      }
    }
  }
}