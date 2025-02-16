package com.world.planner.global.config;

import java.util.List;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Setter
@Configuration
@ConfigurationProperties(prefix = "cors")
@Slf4j
public class WebConfig {

  private List<String> allowedOrigins;

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        if (allowedOrigins == null || allowedOrigins.isEmpty()) {
          log.error("CORS allowedOrigins is null or empty. Please check application.yml or application.properties.");
          throw new IllegalStateException("CORS allowedOrigins 설정이 누락되었습니다.");
        }
        registry.addMapping("/**")
            .allowedOrigins(allowedOrigins.toArray(String[]::new))
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("Authorization", "Content-Type", "X-Requested-With", "Accept") // 허용할 헤더 설정
            .allowCredentials(true);
      }
    };
  }
}