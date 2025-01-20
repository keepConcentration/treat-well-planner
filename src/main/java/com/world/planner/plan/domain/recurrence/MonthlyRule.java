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
@DiscriminatorValue("MONTHLY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonthlyRule extends RecurrenceRule {

  @ElementCollection
  @CollectionTable(name = "recurrence_rule_days", joinColumns = @JoinColumn(name = "recurrence_rule_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "day_of_week")
  private List<DayOfWeek> daysOfWeek;

  @ElementCollection
  @CollectionTable(name = "recurrence_rule_days_of_month", joinColumns = @JoinColumn(name = "recurrence_rule_id"))
  @Column(name = "day_of_month")
  private List<Integer> daysOfMonth;

  public MonthlyRule(int interval, List<DayOfWeek> daysOfWeek, List<Integer> daysOfMonth) {
    super(interval);
    this.daysOfWeek = daysOfWeek;
    this.daysOfMonth = daysOfMonth;
  }

  @Override
  public String getDescription() {
    StringBuilder description = new StringBuilder("Every ")
        .append(getInterval())
        .append(" month(s)");
    if (daysOfWeek != null && !daysOfWeek.isEmpty()) {
      description.append(" on ").append(daysOfWeek);
    }
    if (daysOfMonth != null && !daysOfMonth.isEmpty()) {
      description.append(" on days ").append(daysOfMonth);
    }
    return description.toString();
  }

  @Override
  public boolean isValid() {
    return getInterval() > 0 &&
        (daysOfWeek == null || !daysOfWeek.isEmpty()) &&
        (daysOfMonth == null || daysOfMonth.stream().allMatch(day -> day > 0 && day <= 31));
  }

  @Override
  public boolean isOccurrence(LocalDate date) {
    if (!isValid() || date == null) {
      return false;
    }
    LocalDate startDate = LocalDate.now(); // 기본 발생 시작일
    long monthsBetween = ChronoUnit.MONTHS.between(startDate.withDayOfMonth(1), date.withDayOfMonth(1));

    return monthsBetween >= 0 &&
        monthsBetween % getInterval() == 0 &&
        daysOfMonth.contains(date.getDayOfMonth());
  }

}
