package com.world.planner.global.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("${jwt.secret-key}")
  private String secretKey;

  /**
   * ID 기반 JWT 토큰을 생성합니다.
   *
   * @param id JWT의 subject로 사용될 id
   * @return 생성된 JWT 토큰 문자열
   */
  public String createToken(String id) {
    Key key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS512.getJcaName());

    return Jwts.builder()
        .setSubject(id)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간 유효
        .signWith(key) // SecretKey 객체를 사용해 서명
        .compact();
  }

  /**
   * UUID 기반 JWT 토큰을 생성합니다.
   *
   * @param id JWT의 subject로 사용될 UUID
   * @return 생성된 JWT 토큰 문자열
   */
  public String createToken(UUID id) {
    return createToken(id.toString());
  }

  /**
   * Long 타입 ID를 기반으로 JWT 토큰을 생성합니다.
   *
   * @param id JWT의 subject로 사용될 Long 타입 id
   * @return 생성된 JWT 토큰 문자열
   */
  public String createToken(Long id) {
    return createToken(id.toString());
  }
}
