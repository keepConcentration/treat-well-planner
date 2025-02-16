package com.world.planner.auth.utility;

import com.world.planner.auth.domain.token.RefreshToken;
import com.world.planner.global.config.JwtProvider;
import com.world.planner.global.exception.InvalidJwtTokenException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenValidator {

  /**
   * Refresh Token 검증 (유효성, 만료, 형식 등)
   *
   * @param refreshToken 검증할 Refresh Token 객체
   * @throws InvalidJwtTokenException 토큰이 유효하지 않거나 만료된 경우 발생
   */
  public static void validateRefreshToken(RefreshToken refreshToken) {
    // JWT 서명 및 구조 확인
    JwtProvider.validate(refreshToken.getValue());

    // 토큰 만료 여부 확인
    if (refreshToken.isExpired()) {
      throw new InvalidJwtTokenException("Refresh Token이 만료되었습니다.");
    }
  }

  /**
   * Bearer Token 형식 검증
   *
   * @param authorizationHeader Authorization 헤더에서 전달된 Bearer Token 문자열
   * @return RefreshToken 객체로 변환된 값
   * @throws InvalidJwtTokenException Bearer Token 형식이 잘못된 경우 발생
   */
  public static RefreshToken extractAndValidateBearer(String authorizationHeader) {
    try {
      return RefreshToken.fromBearer(authorizationHeader);
    } catch (IllegalArgumentException e) {
      throw new InvalidJwtTokenException("Authorization 헤더의 Bearer Token 형식이 올바르지 않습니다.");
    }
  }
}