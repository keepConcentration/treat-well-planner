package com.world.planner.auth.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KakaoUser {

  private final Long id;
  private final String nickname;
  private final String email;

  private KakaoUser(Long id, String nickname, String email) {
    this.id = id;
    this.nickname = nickname;
    this.email = email;
  }

  public static KakaoUser create(Long id, String nickname, String email) {
    return new KakaoUser(id, nickname, email);
  }

  // 도메인 로직 추가 가능
  public boolean isValid() {
    return id != null && nickname != null && !nickname.isEmpty();
  }
}