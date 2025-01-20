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
@DiscriminatorValue("YEARLY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class YearlyRule extends RecurrenceRule {

  // Getters for each collection
  @ElementCollection
  @CollectionTable(name = "recurrence_rule_months", joinColumns = @JoinColumn(name = "recurrence_rule_id"))
  @Column(name = "month_of_year")
  private List<Integer> monthsOfYear;

  @ElementCollection
  @CollectionTable(name = "recurrence_rule_days", joinColumns = @JoinColumn(name = "recurrence_rule_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "day_of_week")
  private List<DayOfWeek> daysOfWeek;

  @ElementCollection
  @CollectionTable(name = "recurrence_rule_days_of_month", joinColumns = @JoinColumn(name = "recurrence_rule_id"))
  @Column(name = "day_of_month")
  private List<Integer> daysOfMonth;

  public YearlyRule(int interval, List<Integer> monthsOfYear, List<DayOfWeek> daysOfWeek, List<Integer> daysOfMonth) {
    super(interval);
    this.monthsOfYear = monthsOfYear;
    this.daysOfWeek = daysOfWeek;
    this.daysOfMonth = daysOfMonth;
  }

  @Override
  public String getDescription() {
    StringBuilder description = new StringBuilder("Every ")
        .append(getInterval())
        .append(" year(s)");
    if (monthsOfYear != null && !monthsOfYear.isEmpty()) {
      description.append(" in ").append(monthsOfYear);
    }
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
        (monthsOfYear != null && monthsOfYear.stream().allMatch(month -> month > 0 && month <= 12)) &&
        (daysOfWeek == null || !daysOfWeek.isEmpty()) &&
        (daysOfMonth == null || daysOfMonth.stream().allMatch(day -> day > 0 && day <= 31));
  }

  @Override
  public boolean isOccurrence(LocalDate date) {
    if (!isValid() || date == null) {
      return false;
    }
    LocalDate startDate = LocalDate.now();
    long yearsBetween = ChronoUnit.YEARS.between(startDate.withDayOfYear(1), date.withDayOfYear(1));

    return yearsBetween >= 0 &&
        yearsBetween % getInterval() == 0 &&
        (monthsOfYear == null || monthsOfYear.contains(date.getMonthValue())) &&
        (daysOfMonth == null || daysOfMonth.contains(date.getDayOfMonth())) &&
        (daysOfWeek == null || daysOfWeek.contains(date.getDayOfWeek()));
  }

}
