package com.world.planner.auth.domain.token;

import com.world.planner.global.config.JwtProvider;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public abstract class BaseToken {

  private final String value;
  private final LocalDateTime expiration;

  protected BaseToken(String value, LocalDateTime expiration) {
    if (value == null || value.isEmpty()) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " cannot be null or empty");
    }
    this.value = value;
    this.expiration = expiration;
  }

  public static <T extends BaseToken> T create(String value, LocalDateTime expiration, TokenCreator<T> creator) {
    return creator.create(value, expiration);
  }

  public static <T extends BaseToken> T create(String value, TokenCreator<T> creator) {
    return creator.create(value, JwtProvider.getExpiration(value));
  }

  @Override
  public String toString() {
    return value;
  }

  // 팩토리 함수 인터페이스
  @FunctionalInterface
  public interface TokenCreator<T> {
    T create(String value, LocalDateTime expiration);
  }
}
