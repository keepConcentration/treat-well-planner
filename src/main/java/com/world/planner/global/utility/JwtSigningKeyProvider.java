package com.world.planner.global.utility;

import com.world.planner.global.exception.InvalidJwtTokenException;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtSigningKeyProvider {

  private static Key signingKey;

  /**
   * SigningKey를 설정합니다.
   *
   * @param secretKey 비밀 키
   */
  public static void initialize(String secretKey) {
    if (isInitialized()) {
      log.warn("SigningKeySigningKey는 이미 초기화되었습니다.");
      return;
    }
    signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS512.getJcaName());
  }

  /**
   * SigningKey를 반환합니다.
   *
   * @return SigningKey
   * @throws IllegalStateException 초기화되지 않은 경우 예외 발생
   */
  public static Key getSigningKeyOrThrow() {
    if (!isInitialized()) {
      throw new InvalidJwtTokenException("SigningKey가 초기화되지 않았습니다. 먼저 initialize()를 호출해야 합니다.");
    }
    return signingKey;
  }

  /**
   * SigningKey가 초기화되어 있는지 확인합니다.
   *
   * @return 초기화 여부
   */
  public static boolean isInitialized() {
    return signingKey != null;
  }
}
