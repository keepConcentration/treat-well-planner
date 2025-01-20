package com.world.planner.plan.presentation.dto.response;

import com.world.planner.plan.domain.Plan;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Plan 요약 응답")
public class PlanSummaryResponse {

  @Schema(description = "Plan ID", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID id;

  @Schema(description = "Plan 제목", example = "Weekly Meeting")
  private String title;

  @Schema(description = "Plan 시작 날짜", example = "2023-12-01")
  private LocalDate startDate;

  @Schema(description = "Plan 종료 날짜", example = "2023-12-05")
  private LocalDate endDate;

  public static PlanSummaryResponse fromEntity(Plan plan) {
    return PlanSummaryResponse.builder()
        .id(plan.getId())
        .title(plan.getTitle())
        .startDate(plan.getStartDate())
        .endDate(plan.getEndDate())
        .build();
  }
}
