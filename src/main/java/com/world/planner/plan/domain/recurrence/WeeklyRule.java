package com.world.planner.plan.domain.recurrence;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("WEEKLY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklyRule extends RecurrenceRule {

  @ElementCollection
  @CollectionTable(name = "recurrence_rule_days", joinColumns = @JoinColumn(name = "recurrence_rule_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "day_of_week")
  private List<DayOfWeek> daysOfWeek;

  public WeeklyRule(int interval, List<DayOfWeek> daysOfWeek) {
    super(interval);
    this.daysOfWeek = daysOfWeek;
  }

  @Override
  public String getDescription() {
    return "Every " + getInterval() + " week(s) on " + daysOfWeek;
  }

  @Override
  public boolean isValid() {
    return getInterval() > 0 && daysOfWeek != null && !daysOfWeek.isEmpty();
  }

  @Override
  public boolean isOccurrence(LocalDate date) {
    if (!isValid() || date == null) {
      return false;
    }
    LocalDate startDate = LocalDate.now(); // 기본 발생 시작일 (예: 현재 날짜)
    long weeksBetween = ChronoUnit.WEEKS.between(startDate, date);

    return weeksBetween >= 0 &&
        weeksBetween % getInterval() == 0 &&
        daysOfWeek.contains(date.getDayOfWeek());
  }

}
