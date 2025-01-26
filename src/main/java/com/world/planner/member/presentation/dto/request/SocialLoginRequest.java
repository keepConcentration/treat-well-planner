package com.world.planner.member.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SocialLoginRequest {

  @Schema(description = "소셜 로그인으로 인증된 이메일", example = "user@example.com", required = true)
  private String email;

  @Schema(description = "소셜 로그인으로부터 제공된 사용자 이름", example = "John Doe", required = true)
  private String name;
}