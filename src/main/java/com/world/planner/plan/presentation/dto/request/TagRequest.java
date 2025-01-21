package com.world.planner.plan.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagRequest {

    @Schema(description = "태그 이름", example = "travel")
    @NotBlank(message = "태그 이름은 공백일 수 없습니다.")
    @NotNull(message = "태그 이름은 반드시 입력해야 합니다.")
    private String tagName;
}
