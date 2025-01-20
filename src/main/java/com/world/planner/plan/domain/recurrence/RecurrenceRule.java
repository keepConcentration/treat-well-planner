package com.world.planner.plan.domain.recurrence;

import com.fasterxml.jackson.databind.ser.Serializers.Base;
import com.world.planner.global.event.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "rule_type")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RecurrenceRule extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "recurrence_interval", nullable = false)
  private int interval;

  protected RecurrenceRule(int interval) {
    if (interval <= 0) {
      throw new IllegalArgumentException("Interval must be greater than 0.");
    }
    this.interval = interval;
  }

  public abstract String getDescription(); // 규칙 설명 반환

  public abstract boolean isValid();       // 유효성 검증

  /**
   * 특정 날짜가 해당 반복 규칙에 포함되는지 확인.
   *
   * @param date 확인할 LocalDate
   * @return 포함 여부
   */
  public abstract boolean isOccurrence(LocalDate date);
}
