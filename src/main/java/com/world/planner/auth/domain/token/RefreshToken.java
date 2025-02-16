package com.world.planner.auth.domain.token;

import com.world.planner.global.config.JwtProvider;
import java.time.Duration;
import java.util.UUID;
import lombok.Value;
import java.time.LocalDateTime;

@Value
public class RefreshToken extends BaseToken {

  private static final long REFRESH_TOKEN_VALIDITY_IN_MILLISECONDS = 2592000000L; // 30일

  private RefreshToken(String value, LocalDateTime expiration) {
    super(value, expiration);
  }

  public static RefreshToken create(String value, LocalDateTime expiration) {
    return BaseToken.create(value, expiration, RefreshToken::new);
  }

  public static RefreshToken create(String value) {
    return BaseToken.create(value, RefreshToken::new);
  }

  public static RefreshToken create(UUID memberId) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expirationTime = now.plus(Duration.ofMillis(REFRESH_TOKEN_VALIDITY_IN_MILLISECONDS));

    String tokenValue = JwtProvider.generate(memberId.toString(), expirationTime);
    return create(tokenValue, expirationTime);
  }

  public static RefreshToken fromBearer(String bearerToken) {
    if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
      throw new IllegalArgumentException("Invalid Bearer Token format");
    }

    String tokenValue = bearerToken.substring(7).trim();

    if (tokenValue.isEmpty()) {
      throw new IllegalArgumentException("Token value cannot be empty");
    }

    return create(tokenValue);
  }

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(getExpiration());
  }

  public UUID getMemberId() {
    return UUID.fromString(JwtProvider.getSubject(getValue()));
  }
}