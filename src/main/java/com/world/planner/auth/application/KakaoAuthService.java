package com.world.planner.auth.application;

import com.world.planner.auth.domain.KakaoUser;
import com.world.planner.auth.infrastructure.KakaoApiClient;
import com.world.planner.member.application.MemberService;
import com.world.planner.member.domain.SocialProvider;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoAuthService {

  private final KakaoApiClient kakaoApiClient;

  private final MemberService memberService;

  /**
   * 카카오 OAuth 인증 코드를 사용해 회원을 조회하거나 새롭게 생성합니다.
   *
   * @param authCode 카카오 OAuth 인증 코드
   * @return 사용자의 UUID (회원 고유 식별자)
   */
  public UUID findOrCreateMember(String authCode) {
    // 카카오 OAuth 서비스에서 사용자 정보 조회
    KakaoUser kakaoUser = kakaoApiClient.getUserInfo(authCode);

    // 사용자의 소셜 ID와 소셜 제공자를 기반으로 회원 조회 또는 생성
    return memberService.findOrCreateMemberBySocialAccount(
        String.valueOf(kakaoUser.getId()), SocialProvider.KAKAO);
  }
}