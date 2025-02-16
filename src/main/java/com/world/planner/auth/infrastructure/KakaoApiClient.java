package com.world.planner.auth.infrastructure;

import com.world.planner.auth.domain.KakaoUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class KakaoApiClient {

  private static final RestTemplate REST_TEMPLATE = new RestTemplate();

  @Value("${kakao.auth.client-id}")
  private String clientId;

  @Value("${kakao.auth.redirect-uri}")
  private String redirectUri;

  private String getAccessToken(String code) {
    // 요청 헤더 설정
    HttpHeaders headers = createHeaders(MediaType.APPLICATION_FORM_URLENCODED, null);

    // 요청 Body 설정
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", clientId);
    body.add("redirect_uri", redirectUri);
    body.add("code", code);
    // 만약 client_secret이 필요한 경우, 환경 변수에서 추가 설정
    // body.add("client_secret", clientSecret);

    // Access Token 요청
    final String tokenRequestUrl = "https://kauth.kakao.com/oauth/token";
    Map<String, Object> response = executePostRequest(tokenRequestUrl, new HttpEntity<>(body, headers), Map.class)
        .orElseThrow(() -> new IllegalStateException("Failed to retrieve Kakao tokens: No tokens in response."));

    // 응답 데이터 처리
    if (!response.containsKey("access_token")) {
      throw new IllegalStateException("Invalid Kakao token response: Missing required tokens.");
    }

    return response.get("access_token").toString();
  }

  public KakaoUser getUserInfo(String code) {
    String accessToken = getAccessToken(code);

    // 사용자 정보 요청을 위한 HTTP Header만 구성
    HttpHeaders headers = createHeaders(null, accessToken);
    HttpEntity<?> request = new HttpEntity<>(headers);

    // 사용자 정보 요청 및 유저 생성
    final String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
    return executeGetRequest(userInfoUrl, request, Map.class)
        .map(this::mapToKakaoUser)
        .orElseThrow(() -> new IllegalStateException("Failed to retrieve user info: Response was invalid."));
  }

  private HttpHeaders createHeaders(MediaType contentType, String accessToken) {
    HttpHeaders headers = new HttpHeaders();
    if (contentType != null) {
      headers.setContentType(contentType);
    }
    if (accessToken != null) {
      headers.set("Authorization", "Bearer " + accessToken);
    }
    return headers;
  }

  private KakaoUser mapToKakaoUser(Map<String, Object> userInfo) {
    // 사용자 정보가 없거나 ID가 없으면 예외 처리
    if (userInfo == null || !userInfo.containsKey("id")) {
      log.error("Failed to parse user info: {}", userInfo);
      throw new IllegalStateException("Failed to retrieve valid user info.");
    }

    // 이메일 처리
    Map<String, String> kakaoAccount = (Map<String, String>) userInfo.get("kakao_account");
    String email = kakaoAccount != null ? kakaoAccount.get("email") : null;

    // 닉네임 처리
    Map<String, Object> properties = (Map<String, Object>) userInfo.get("properties");
    String nickname = properties != null ? (String) properties.getOrDefault("nickname", "Unknown") : "Unknown";

    return KakaoUser.create(
        (Long) userInfo.get("id"),
        nickname,
        email
    );
  }

  private <T> Optional<T> executePostRequest(String url, HttpEntity<?> request, Class<T> responseType) {
    try {
      ResponseEntity<T> response = REST_TEMPLATE.exchange(url, HttpMethod.POST, request, responseType);
      return Optional.ofNullable(response.getBody());
    } catch (HttpClientErrorException e) {
      handleHttpClientError(url, e);
      return Optional.empty();
    } catch (Exception e) {
      handleUnexpectedError(url, e);
      return Optional.empty();
    }
  }

  private <T> Optional<T> executeGetRequest(String url, HttpEntity<?> request, Class<T> responseType) {
    try {
      ResponseEntity<T> response = REST_TEMPLATE.exchange(url, HttpMethod.GET, request, responseType);
      return Optional.ofNullable(response.getBody());
    } catch (HttpClientErrorException e) {
      handleHttpClientError(url, e);
      return Optional.empty();
    } catch (Exception e) {
      handleUnexpectedError(url, e);
      return Optional.empty();
    }
  }

  private void handleHttpClientError(String url, HttpClientErrorException e) {
    log.error("HTTP Client Error during request to {}: Status Code = {}, Response Body = {}",
        url, e.getStatusCode(), e.getResponseBodyAsString(), e);
    throw new IllegalStateException("HTTP request failed: " + e.getMessage(), e);
  }

  private void handleUnexpectedError(String url, Exception e) {
    log.error("Unexpected error during request to {}", url, e);
    throw new IllegalStateException("Unexpected error during HTTP request.", e);
  }
}