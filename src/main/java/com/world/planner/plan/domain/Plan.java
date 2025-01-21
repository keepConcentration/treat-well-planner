package com.world.planner.plan.domain;

import com.world.planner.global.event.BaseEntity;
import com.world.planner.plan.domain.recurrence.RecurrenceRule;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plans")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 100)
  private String title;

  @Column(nullable = false, length = 500)
  private String description;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
          name = "plan_tags",
          joinColumns = @JoinColumn(name = "plan_id"),
          inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private Set<Tag> tags = new HashSet<>();

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "recurrence_rule_id")
  private RecurrenceRule recurrenceRule;

  private Plan(String title, String description, LocalDate startDate, LocalDate endDate) {
    if (endDate != null && startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date must be before or equal to the end date.");
    }
    this.title = title;
    this.description = description;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public static Plan create(String title, String description, LocalDate startDate, LocalDate endDate) {
    return new Plan(title, description, startDate, endDate);
  }

  public boolean hasRecurrenceRule() {
    return recurrenceRule != null;
  }

  public void updateDetails(String title, String description, LocalDate startDate, LocalDate endDate) {
    if (endDate != null && startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date must be before or equal to the end date.");
    }
    this.title = title;
    this.description = description;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public void addRecurrenceRule(RecurrenceRule rule) {
    if (rule == null || !rule.isValid()) {
      throw new IllegalArgumentException("Invalid recurrence rule");
    }
    this.recurrenceRule = rule;
  }

  public boolean isSomeday() {
    return startDate == null && endDate == null;
  }

  public void removeRecurrenceRule() {
    this.recurrenceRule = null;
  }

  public void addTag(Tag tag) {
    if (tag != null) {
      tags.add(tag);
      tag.getPlans().add(this); // 양방향 관계 설정
    }
  }

  public void removeTag(Tag tag) {
    if (tag != null) {
      tags.remove(tag);
      tag.getPlans().remove(this); // 양방향 관계 제거
    }
  }

}
