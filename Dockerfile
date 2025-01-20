# Java 21 이미지를 이용한 Spring Boot 빌드 및 실행
FROM eclipse-temurin:21-jdk-alpine

# 워킹 디렉토리 설정
WORKDIR /app

# 실행 파일(JAR) 복사
COPY build/libs/treat-well-planner-0.0.1-SNAPSHOT.jar app.jar

# 환경 변수 설정 (기본값)
ENV SPRING_PROFILES_ACTIVE=local

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]