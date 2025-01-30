package com.world.planner.auth.presentation;

import com.world.planner.auth.application.KakaoAuthService;
import com.world.planner.auth.presentation.dto.request.KakaoAuthRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Kakao Authentication", description = "카카오 로그인 및 사용자 인증 API")
@RestController
@RequestMapping("/api/auth/kakao")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoAuthRestController {

  private final KakaoAuthService kakaoAuthService;

  @Operation(
      summary = "카카오 로그인 콜백 처리",
      description = "카카오 OAuth 인증 과정에서 발급된 인가 코드를 사용하여 사용자 인증을 처리한 후 JWT 토큰을 반환합니다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "카카오 사용자 인증 및 JWT 발급 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 (예: 인가 코드 누락)"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @PostMapping
  public ResponseEntity<?> authenticateAndGenerateToken(
      @Parameter(description = "카카오 OAuth 인증을 통해 발급된 인가 코드", required = true)
      @RequestBody KakaoAuthRequest request // DTO를 통한 요청 본문 매핑
  ) {
    String code = request.getCode();
    if (code == null || code.isEmpty()) {
      return ResponseEntity.badRequest().body("인가 코드를 제공하지 않았습니다.");
    }
    return ResponseEntity.ok(Map.of("token", kakaoAuthService.generateJwtWithKakaoAuthCode(code)));
  }
}