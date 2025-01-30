package com.world.planner.auth.application;

import com.world.planner.auth.domain.KakaoUser;
import com.world.planner.auth.infrastructure.KakaoApiClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoAuthService {

  private final KakaoApiClient kakaoApiClient;

  public KakaoUser authenticateKakaoUser(String code) {
    // Access Token 발급
    String accessToken = kakaoApiClient.getAccessToken(code);

    // 사용자 정보 가져오기
    return kakaoApiClient.getUserInfo(accessToken);
  }
}