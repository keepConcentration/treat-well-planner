package com.world.planner.global.config;

import java.util.List;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Setter
@Configuration
@ConfigurationProperties(prefix = "cors")
@Slf4j
public class WebConfig implements WebMvcConfigurer {

  private List<String> allowedOrigins;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    if (allowedOrigins == null || allowedOrigins.isEmpty()) {
      log.error("CORS allowedOrigins is null or empty. Please check application.yml or application.properties.");
      throw new IllegalStateException("CORS allowedOrigins 설정이 누락되었습니다.");
    }
    registry.addMapping("/**")
        .allowedOrigins(allowedOrigins.toArray(new String[0])) // 프로파일에 따라 Origin이 동적으로 설정됨
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*");
  }
}
