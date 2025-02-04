package com.world.planner.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/h2-console/**").permitAll() // H2 Console 요청 허용
            .requestMatchers("/api/auth/kakao/login").permitAll() // 해당 엔드포인트 허용
            .requestMatchers("/api/auth/validate-token").permitAll()
            .requestMatchers("/api/auth/refresh-token").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll() // Swagger 관련 경로 허용
            .requestMatchers("/api/members/social-login").permitAll()
            .anyRequest().authenticated() // 다른 요청은 인증 필요
        )
        .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
        .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable)); // IFrame 방어 비활성화

    return http.build();
  }
}