package com.world.planner.plan.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class CategoryResponse {

  @Schema(description = "카테고리 ID", example = "c9b6bb12-7b1a-4b91-a5fd-92d5135cf3a3")
  private UUID id;

  @Schema(description = "카테고리 이름", example = "운동")
  private String name;

  @Schema(description = "삭제 여부", example = "false")
  private boolean deleted;

  @Schema(description = "카테고리에 연결된 Plan 요약 목록")
  private List<PlanSummaryResponse> plans;
}