package com.world.planner.auth.application;

import com.world.planner.auth.domain.KakaoUser;
import com.world.planner.auth.infrastructure.KakaoApiClient;
import com.world.planner.global.config.JwtTokenProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoAuthService {

  private final KakaoApiClient kakaoApiClient;

  private final JwtTokenProvider jwtTokenProvider;

  /**
   * 카카오 OAuth 인증 코드를 사용하여 JWT 토큰을 생성합니다.
   *
   * @param code 카카오 OAuth 인증을 통해 발급된 인가 코드
   * @return 생성된 JWT 토큰 문자열
   */
  public String generateJwtWithKakaoAuthCode(String code) {
    // Access Token 발급
    String accessToken = kakaoApiClient.getAccessToken(code);

    // 사용자 정보 가져오기
    KakaoUser user = kakaoApiClient.getUserInfo(accessToken);

    // JWT 토큰 생성 후 반환
    return jwtTokenProvider.createToken(user.getId());
  }
}