package com.world.planner.plan.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryRequest {

  @Schema(description = "카테고리 이름", example = "운동")
  @NotBlank(message = "카테고리 이름은 반드시 입력해야 합니다.")
  private String name;
}