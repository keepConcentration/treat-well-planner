package com.world.planner.auth.presentation;

import com.world.planner.auth.application.KakaoAuthService;
import com.world.planner.auth.application.MemberTokenService;
import com.world.planner.auth.presentation.dto.response.JwtResponse;
import com.world.planner.auth.presentation.dto.request.KakaoAuthRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 카카오 로그인 및 JWT 생성 관련 컨트롤러
 */
@Tag(name = "Kakao Authentication", description = "카카오 로그인 및 사용자 인증 API")
@RestController
@RequestMapping("/api/auth/kakao")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoAuthRestController {

  private final KakaoAuthService kakaoAuthService;

  private final MemberTokenService memberTokenService;
  /**
   * 카카오 로그인 및 JWT 생성 API
   *<p></p>
   * OAuth 인증 코드를 사용하여 회원을 확인(생성 포함)하고,
   * 애플리케이션의 JWT 및 Refresh Token을 반환합니다.
   * </p>
   * @param request 카카오 인증 요청 (code 포함)
   * @return JWT 및 Refresh Token
   */
  @Operation(
      summary = "카카오 로그인 및 JWT 발급",
      description = "카카오 OAuth 인증 코드를 사용하여 회원을 인증(또는 생성)하고, JWT 및 Refresh Token을 반환합니다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "JWT 및 Refresh Token 발급 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 (예: 인가 코드 누락)"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")
  })
  @PostMapping("/login")
  public ResponseEntity<JwtResponse> loginWithKakao(
      @Parameter(description = "카카오 OAuth 인증을 통해 발급된 인가 코드", required = true)
      @Valid @RequestBody KakaoAuthRequest request
  ) {
    String authCode = request.getCode();
    if (authCode == null || authCode.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    // 회원 조회 또는 생성
    UUID memberId = kakaoAuthService.findOrCreateMember(authCode);

    // JWT 및 Refresh Token 발급
    JwtResponse response = memberTokenService.createAndStoreJwts(memberId);

    return ResponseEntity.ok(response);
  }
}