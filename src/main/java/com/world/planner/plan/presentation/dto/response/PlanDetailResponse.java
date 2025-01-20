package com.world.planner.plan.presentation.dto.response;

import com.world.planner.plan.domain.Plan;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Plan 상세 응답")
public class PlanDetailResponse {

  @Schema(description = "Plan 제목", example = "Team Meeting")
  private String title;

  @Schema(description = "Plan 설명", example = "Discuss Q4 goals")
  private String description;

  @Schema(description = "Plan 시작 날짜", example = "2023-12-01")
  private LocalDate startDate;

  @Schema(description = "Plan 종료 날짜", example = "2023-12-05")
  private LocalDate endDate;

  public static PlanDetailResponse fromEntity(Plan plan) {
    return PlanDetailResponse.builder()
        .title(plan.getTitle())
        .description(plan.getDescription())
        .startDate(plan.getStartDate())
        .endDate(plan.getEndDate())
        .build();
  }
}
