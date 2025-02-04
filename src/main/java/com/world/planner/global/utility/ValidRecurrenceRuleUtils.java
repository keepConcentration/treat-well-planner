package com.world.planner.global.utility;

import com.world.planner.plan.domain.recurrence.RecurrenceRuleType;
import java.time.LocalDate;

public class ValidRecurrenceRuleUtils {

  public static boolean isValid(String ruleType) {
    return RecurrenceRuleType.isValid(ruleType);
  }

  /**
   * 시작 날짜와 종료 날짜가 유효한지 확인한다.
   */
  public static void validateDateRange(LocalDate startDate, LocalDate endDate) {
    if (!isValidDateRange(startDate, endDate)) {
      throw new IllegalArgumentException("Invalid date range");
    }
  }

  /**
   * 시작 날짜와 종료 날짜가 유효한지 확인한다.
   * - 둘 다 null이면 "언젠가 할 일"로 간주, 유효.
   * - 둘 중 하나만 null이거나, startDate가 endDate 이후인 경우는 무효.
   */
  public static boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
    // "언젠가 할 일"인 경우 유효
    if (startDate == null && endDate == null) {
      return true;
    }

    // startDate와 endDate 중 하나만 null인 경우 무효
    if (startDate == null || endDate == null) {
      return false;
    }

    // startDate가 endDate 이후인 경우 무효
    return !startDate.isAfter(endDate);
  }

}