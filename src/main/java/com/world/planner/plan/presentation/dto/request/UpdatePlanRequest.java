package com.world.planner.plan.presentation.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import lombok.Getter;

@Getter
@Schema(description = "Plan 수정 요청")
public class UpdatePlanRequest {

  @NotBlank(message = "제목을 입력해야 합니다.")
  @Schema(description = "Plan 제목", example = "Weekly Team Meeting")
  private String title;

  @NotBlank(message = "설명을 입력해야 합니다.")
  @Schema(description = "Plan 설명", example = "Team meeting to discuss goals and updates")
  private String description;

  @Schema(description = "Plan 시작 날짜", example = "2023-12-01")
  private LocalDate startDate;

  @Schema(description = "Plan 종료 날짜", example = "2023-12-05")
  private LocalDate endDate;
}
