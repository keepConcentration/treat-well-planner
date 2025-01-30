package com.world.planner.auth.presentation.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoAuthRequest {
  private String code; // 카카오 OAuth 인증 코드
}