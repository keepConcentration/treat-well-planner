package com.world.planner.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/h2-console/**").permitAll() // H2 Console 요청 허용
            .requestMatchers("/api/auth/kakao").permitAll() // 해당 엔드포인트 허용
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll() // Swagger 관련 경로 허용
            .requestMatchers("/api/members/social-login").permitAll()
            .anyRequest().authenticated() // 다른 요청은 인증 필요
        )
        .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
        .cors(cors -> {}) // CORS 설정 활성화
        .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable)); // IFrame 방어 비활성화

    return http.build();
  }

  // CORS 설정 추가
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 엔드포인트에 대해
            .allowedOrigins("http://localhost:3000") // 프론트엔드 React 서버 도메인
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
            .allowCredentials(true); // 쿠키 및 인증정보 허용
      }
    };
  }
}
