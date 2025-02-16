package com.world.planner.auth.domain.token;

import com.world.planner.global.config.JwtProvider;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Value;

@Value
public class AccessToken extends BaseToken {

  private static final long ACCESS_TOKEN_VALIDITY_IN_MILLISECONDS = 10000L;   // 1800000L; // 30분

  private AccessToken(String value, LocalDateTime expiration) {
    super(value, expiration);
  }

  public static AccessToken create(String value, LocalDateTime expiration) {
    return BaseToken.create(value, expiration, AccessToken::new);
  }

  public static AccessToken create(UUID memberId) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expirationTime = now.plus(Duration.ofMillis(ACCESS_TOKEN_VALIDITY_IN_MILLISECONDS));

    String tokenValue = JwtProvider.generate(memberId.toString(), expirationTime);
    return create(tokenValue, expirationTime);
  }

  public static AccessToken create(String value) {
    return BaseToken.create(value, AccessToken::new);
  }

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(getExpiration());
  }
}
