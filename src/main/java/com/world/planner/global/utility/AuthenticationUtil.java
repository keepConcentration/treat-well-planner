package com.world.planner.global.utility;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

// Spring Security와 직접 연결된 인증 관련 작업만 처리
@Component
public class AuthenticationUtil {

  /**
   * 현재 인증된 사용자의 이메일 가져오기
   *    * @return String
   */
  public String getAuthenticatedEmail() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}
