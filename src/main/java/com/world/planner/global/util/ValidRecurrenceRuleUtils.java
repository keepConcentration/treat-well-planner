package com.world.planner.global.util;

import com.world.planner.plan.domain.recurrence.RecurrenceRuleType;

public class ValidRecurrenceRuleUtils {

  public static boolean isValid(String ruleType) {
    return RecurrenceRuleType.isValid(ruleType);
  }
}