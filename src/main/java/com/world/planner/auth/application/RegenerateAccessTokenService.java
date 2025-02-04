package com.world.planner.auth.application;

import com.world.planner.auth.domain.MemberToken;
import com.world.planner.auth.domain.token.AccessToken;
import com.world.planner.auth.domain.token.RefreshToken;
import com.world.planner.auth.infrastructure.repository.MemberTokenRepository;
import com.world.planner.auth.utility.TokenValidator;
import com.world.planner.global.exception.InvalidJwtTokenException;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Access Token 재발급 관련 비즈니스 로직을 처리하는 서비스 클래스
 * <p>
 * 이 서비스는 클라이언트로부터 제공받은 Refresh Token의 유효성을 검증한 후,
 * Access Token을 재발급하는 역할을 담당합니다.
 * <p/>
 * 주요 역할:
 * - Refresh Token의 서명 및 유효성 검증
 * - 데이터베이스에 저장된 Refresh Token과 비교
 * - Refresh Token의 만료 여부 검증
 * - 새로운 Access Token 발급
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RegenerateAccessTokenService {

  private final MemberTokenRepository memberServiceTokenRepository;

  /**
   * Refresh Token을 기반으로 Access Token을 재발급합니다.
   * <p>
   * 주어진 Refresh Token의 서명, 일치 여부, 만료 여부를 검증한 후, 새로운 Access Token을 생성합니다.
   * </p>
   *
   * @param refreshToken 클라이언트가 제공한 Refresh Token
   * @return 새로 발급된 Access Token
   * @throws InvalidJwtTokenException Refresh Token이 유효하지 않거나 만료되었거나 저장된 값과 일치하지 않는 경우 발생
   */
  @Transactional
  public AccessToken regenerateAccessToken(RefreshToken refreshToken) {
    // Refresh Token 유효성 검증
    TokenValidator.validateRefreshToken(refreshToken);

    // Refresh Token에서 MemberId 추출
    UUID memberId = refreshToken.getMemberId();

    // DB에서 Member의 Refresh Token 정보 확인
    MemberToken storedToken = memberServiceTokenRepository.findByMemberId(memberId)
        .orElseThrow(() -> new InvalidJwtTokenException("저장된 토큰이 없습니다."));

    if (!storedToken.getRefreshToken().equals(refreshToken.getValue())) {
      throw new InvalidJwtTokenException("토큰이 일치하지 않습니다.");
    }

    // 새로 발급된 Access Token 반환
    return AccessToken.create(memberId);
  }
}
