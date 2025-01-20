package com.world.planner.plan.presentation.dto.request;

import com.world.planner.plan.domain.recurrence.RecurrenceRuleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.DayOfWeek;
import java.util.List;

@Getter
@Schema(description = "반복 규칙 요청")
public class RecurrenceRuleRequest {

  @NotNull(message = "반복 규칙 타입(RuleType)은 반드시 입력해야 합니다.")
  @Schema(description = "반복 규칙 타입 (DAILY, WEEKLY, MONTHLY, YEARLY 중 하나)", example = "DAILY")
  private RecurrenceRuleType ruleType;

  @Min(value = 1, message = "반복 주기는 최소 1 이상이어야 합니다.")
  @Schema(description = "반복 주기 (1 이상)", example = "2")
  private int interval;

  @Schema(description = "반복 요일 리스트 (Optional)", example = "[\"MONDAY\", \"WEDNESDAY\", \"FRIDAY\"]")
  private List<DayOfWeek> daysOfWeek;

  @Schema(description = "반복 날짜 리스트 (Optional)", example = "[1, 15, 30]")
  private List<Integer> daysOfMonth;

  @Schema(description = "반복 월 리스트 (Optional)", example = "[1, 6, 12]")
  private List<Integer> monthsOfYear;

}