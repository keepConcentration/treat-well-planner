package com.world.planner.plan.domain.recurrence;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("DAILY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyRule extends RecurrenceRule {

  public DailyRule(int interval) {
    super(interval);
  }

  @Override
  public String getDescription() {
    return "Every " + getInterval() + " day(s)";
  }

  @Override
  public boolean isValid() {
    return getInterval() > 0;
  }

  /**
   * 주어진 날짜가 반복 규칙에 속하는지 확인
   */
  @Override
  public boolean isOccurrence(LocalDate date) {
    // interval이 유효하지 않거나 date가 null이 아닌지 확인
    if (date == null || !isValid()) {
      return false;
    }

    LocalDate startDate = LocalDate.now(); // 발생 시작일 (기본값으로 LocalDate.now() 사용)
    long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, date);

    return daysBetween >= 0 && daysBetween % getInterval() == 0;
  }
}
