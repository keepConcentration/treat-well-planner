package com.world.planner.member.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateMemberRequest {

  @Schema(description = "수정할 회원의 이름", example = "Jane Doe", required = true)
  private String name;
}