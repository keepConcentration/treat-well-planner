package com.world.planner.plan.presentation.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;

@Getter
@Schema(description = "새 Plan 생성 요청")
public class CreatePlanRequest {

  @NotBlank(message = "Plan 제목은 반드시 입력해야 합니다.")
  @Schema(description = "Plan 제목", example = "Weekly Meeting")
  private String title;

  @NotBlank(message = "Plan 설명은 반드시 입력해야 합니다.")
  @Schema(description = "Plan 설명", example = "Discussion about project updates")
  private String description;

  @NotNull(message = "Plan 시작 날짜는 반드시 입력해야 합니다.")
  @Schema(description = "Plan 시작 날짜", example = "2023-12-01")
  private LocalDate startDate;

  @NotNull(message = "Plan 종료 날짜는 반드시 입력해야 합니다.")
  @Schema(description = "Plan 종료 날짜", example = "2023-12-05")
  private LocalDate endDate;
}
