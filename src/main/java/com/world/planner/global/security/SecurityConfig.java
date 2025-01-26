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
            .anyRequest().permitAll()
        )
        .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
        .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable)); // IFrame 방어 비활성화

    return http.build();
  }
}
