package com.world.planner.plan.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TagResponse {

    @Schema(description = "태그 이름", example = "travel")
    private String tagName;
}
