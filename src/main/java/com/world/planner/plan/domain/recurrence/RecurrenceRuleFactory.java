package com.world.planner.plan.domain.recurrence;

import java.time.DayOfWeek;
import java.util.List;

public class RecurrenceRuleFactory {

  /**
   * DailyRule 생성
   *
   * @param interval 반복 주기
   * @return DailyRule
   */
  public static RecurrenceRule createDailyRule(int interval) {
    return new DailyRule(interval);
  }

  /**
   * WeeklyRule 생성
   *
   * @param interval 반복 주기
   * @param daysOfWeek 반복 요일
   * @return WeeklyRule
   */
  public static RecurrenceRule createWeeklyRule(int interval, List<DayOfWeek> daysOfWeek) {
    return new WeeklyRule(interval, daysOfWeek);
  }

  /**
   * MonthlyRule 생성
   *
   * @param interval 반복 주기
   * @param daysOfWeek 특정한 요일에 반복 (선택적)
   * @param daysOfMonth 특정한 날짜에 반복 (선택적)
   * @return MonthlyRule
   */
  public static RecurrenceRule createMonthlyRule(int interval, List<DayOfWeek> daysOfWeek, List<Integer> daysOfMonth) {
    return new MonthlyRule(interval, daysOfWeek, daysOfMonth);
  }

  /**
   * YearlyRule 생성
   *
   * @param interval 반복 주기
   * @param monthsOfYear 반복되는 월 (선택적)
   * @param daysOfWeek 특정한 요일에 반복 (선택적)
   * @param daysOfMonth 특정한 날짜에 반복 (선택적)
   * @return YearlyRule
   */
  public static RecurrenceRule createYearlyRule(
      int interval,
      List<Integer> monthsOfYear,
      List<DayOfWeek> daysOfWeek,
      List<Integer> daysOfMonth
  ) {
    return new YearlyRule(interval, monthsOfYear, daysOfWeek, daysOfMonth);
  }
}
