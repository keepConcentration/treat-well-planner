package com.world.planner.plan.domain.recurrence;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum RecurrenceRuleType {
  DAILY("매일 반복"),
  WEEKLY("매주 반복"),
  MONTHLY("매월 반복"),
  YEARLY("매년 반복");

  private final String description;
  RecurrenceRuleType(String description) {
    this.description = description;
  }

  /**
   * 지정된 값이 유효한 RecurrenceRuleType인지 확인
   *
   * @param value 검증할 값
   * @return 유효하면 true, 그렇지 않으면 false
   */
  public static boolean isValid(String value) {
    return Arrays.stream(RecurrenceRuleType.values())
        .anyMatch(type -> type.name().equalsIgnoreCase(value));
  }

  /**
   * String 값을 RecurrenceRuleType으로 변환
   *
   * @param value 변환할 값
   * @return 매칭되는 RecurrenceRuleType
   * @throws IllegalArgumentException 잘못된 값인 경우 예외 발생
   */
  public static RecurrenceRuleType fromString(String value) {
    return Arrays.stream(RecurrenceRuleType.values())
        .filter(type -> type.name().equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid RecurrenceRuleType: " + value));
  }
}
