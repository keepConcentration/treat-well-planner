package com.world.planner.auth.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "카카오 인증 요청 객체")
public class KakaoAuthRequest {

  @Schema(description = "카카오 OAuth 인증 과정에서 발급된 코드", example = "abc123xyz")
  private String code; // 카카오 OAuth 인증 코드
}