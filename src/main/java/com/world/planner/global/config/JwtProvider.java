package com.world.planner.global.config;

import com.world.planner.global.exception.InvalidJwtTokenException;
import com.world.planner.global.utility.JwtSigningKeyProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT 생성 및 검증을 담당하는 유틸리티 컴포넌트 클래스
 * <p>
 * - AccessToken 및 RefreshToken의 생성, 서명, 유효성 검증 메서드가 포함되어 있음.
 * - 애플리케이션 전체적으로 JWT 관련 작업을 지원.
 * </p>
 */
@Component
public final class JwtProvider {

  @Value("${jwt.secret-key}")
  private String secretKey;

  /**
   * PostConstruct 애너테이션을 활용해 JWT 서명 키를 초기화합니다.
   */
  @PostConstruct
  private void init() {
    JwtSigningKeyProvider.initialize(secretKey);
  }

  /**
   * JWT를 생성합니다.
   *
   * @param subject 토큰의 주체 (보통 사용자 ID)
   * @param expiration 토큰 만료 시간
   * @return 생성된 JWT
   */
  public static String generate(String subject, LocalDateTime expiration) {
    Date now = Date.from(LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant());
    Date expirationDate = Date.from(expiration.atZone(java.time.ZoneId.systemDefault()).toInstant());

    return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(now)
        .setExpiration(expirationDate)
        .signWith(JwtSigningKeyProvider.getSigningKeyOrThrow())
        .compact();
  }

  /**
   * 제공된 JWT의 유효성을 검증합니다.
   * <p>
   * 유효하지 않거나 서명, 포맷, 만료 등 여러 문제가 발견되면 `InvalidJwtTokenException`을 발생시킵니다.
   * </p>
   * @param token 검증할 JWT
   * @throws InvalidJwtTokenException 유효하지 않은 토큰인 경우 예외 발생
   */
  public static void validate(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(JwtSigningKeyProvider.getSigningKeyOrThrow())
          .build()
          .parseClaimsJws(token);
    } catch (ExpiredJwtException e) {
      throw new InvalidJwtTokenException("토큰이 만료되었습니다.");
    } catch (SecurityException e) {
      throw new InvalidJwtTokenException("잘못된 토큰 서명입니다.");
    } catch (MalformedJwtException e) {
      throw new InvalidJwtTokenException("토큰 형식이 올바르지 않습니다.");
    } catch (InvalidJwtTokenException e) {
      throw e;
    } catch (Exception e) {
      throw new InvalidJwtTokenException("유효하지 않은 토큰입니다.");
    }
  }

  /**
   * JWT 토큰에서 클레임(Claims) 객체를 추출합니다.
   * <p>
   * 클레임은 JWT 본문에 포함된 데이터이며, 여기에는 토큰 발행자, 주체, 만료 시간 등이 포함될 수 있습니다.
   * </p>
   * @param token 클레임을 추출할 JWT 토큰
   * @return 추출된 클레임 객체
   * @throws InvalidJwtTokenException 유효하지 않은 토큰이거나 클레임 파싱 실패 시 예외 발생
   */
  private static Claims parseClaims(String token) {
    try {
      // 토큰에서 클레임(Claims) 정보 추출
      return Jwts.parserBuilder()
          .setSigningKey(JwtSigningKeyProvider.getSigningKeyOrThrow())
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (InvalidJwtTokenException e) {
      throw e;
    } catch (Exception ex) {
      // 클레임 파싱 오류 처리
      throw new InvalidJwtTokenException("토큰에서 클레임 정보를 추출할 수 없습니다.: " + ex.getMessage());
    }
  }

  /**
   * JWT 토큰에서 주체(Subject)를 추출합니다.
   * <p>
   * 주체는 JWT에 설정된 사용자 식별자 등의 고유 데이터로, 클레임의 하나로 저장됩니다.
   * </p>
   * @param token 주체를 추출할 JWT 토큰
   * @return 추출된 주체 정보 (보통 사용자 식별자)
   * @throws InvalidJwtTokenException 주체 추출 실패 시 예외 발생
   */
  public static String getSubject(String token) {
    try {
      // 클레임에서 주체(subject) 정보 가져오기
      return parseClaims(token).getSubject();
    } catch (Exception ex) {
      // 주체 정보 추출 실패 시 예외 처리
      throw new InvalidJwtTokenException("토큰에서 주체 정보를 추출할 수 없습니다.: " + ex.getMessage());
    }
  }

  /**
   * JWT 토큰에서 만료시간을 추출합니다.
   *
   * @param token 만료시간을 추출할 JWT 토큰
   * @return LocalDateTime 형태의 만료 시간
   * @throws InvalidJwtTokenException 유효하지 않은 토큰이거나 만료 시간 추출 실패 시 예외 발생
   */
  public static LocalDateTime getExpiration(String token) {
    try {
      // 클레임에서 만료 시간(expiration) 추출
      Date expiration = parseClaims(token).getExpiration();
      return expiration.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
    } catch (Exception ex) {
      throw new InvalidJwtTokenException("토큰에서 만료 시간을 추출할 수 없습니다.: " + ex.getMessage());
    }
  }
}
