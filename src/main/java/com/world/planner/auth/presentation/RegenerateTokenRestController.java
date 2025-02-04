package com.world.planner.auth.presentation;

import com.world.planner.auth.application.RegenerateAccessTokenService;
import com.world.planner.auth.domain.token.AccessToken;
import com.world.planner.auth.domain.token.RefreshToken;
import com.world.planner.auth.utility.TokenValidator;
import com.world.planner.global.exception.InvalidJwtTokenException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Refresh Token을 사용해 Access Token을 재발급하는 REST API 컨트롤러
 * <p>
 * 이 컨트롤러는 클라이언트로부터 Bearer 형식의 Refresh Token을 받아 새 Access Token을 반환합니다.
 * Refresh Token의 유효성 검증과 데이터베이스 내 저장된 정보 확인 과정에서 유효하지 않은 경우 예외를 통해 상세한 오류를 제공합니다.
 * </p>
 * 특징:
 * <ul>
 *   <li>클라이언트는 Authorization 헤더에 Refresh Token을 Bearer 형식으로 담아 요청해야 합니다.</li>
 *   <li>Refresh Token 유효성 검증 실패 시, <code>401 Unauthorized</code> 상태 코드로 응답합니다.</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/auth/refresh-token")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "Auth", description = "인증 및 토큰 관련 API")
public class RegenerateTokenRestController {

  private final RegenerateAccessTokenService regenerateAccessTokenService;

  /**
   * Refresh Token을 통해 새 Access Token을 생성합니다.
   * <p>
   * 이 메서드는 Refresh Token(Bearer 형식)을 기반으로 Access Token을 생성하고 반환합니다.
   * Refresh Token이 유효하지 않거나 만료되었거나 저장된 값과 일치하지 않는 경우 {@link com.world.planner.global.exception.InvalidJwtTokenException}을 발생시킵니다.
   * </p>
   *
   * @param refreshTokenHeader Authorization 헤더에 포함된 Bearer 형식의 Refresh Token
   * @return 새로 발급된 Access Token
   * @throws InvalidJwtTokenException Refresh Token이 유효하지 않거나 만료되었거나 저장된 값과 일치하지 않는 경우 발생
   */
  @Operation(
      summary = "토큰 갱신",
      description = "Refresh Token을 통해 새로운 Access Token을 생성합니다."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "새로운 Access Token 반환",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = AccessToken.class)
          )
      ),
      @ApiResponse(
          responseCode = "401",
          description = "유효하지 않은 Refresh Token",
          content = @Content(mediaType = "application/json")
      ),
      @ApiResponse(
          responseCode = "500",
          description = "서버 내부 오류",
          content = @Content(mediaType = "application/json")
      )
  })
  @PostMapping
  public ResponseEntity<AccessToken> regenerateToken(
      @Parameter(
          name = "Authorization",
          description = "Bearer 형식의 Refresh Token",
          required = true,
          example = "Bearer your_refresh_token"
      )
      @RequestHeader("Authorization") String refreshTokenHeader) {

    // Bearer 형식에서 RefreshToken 추출 및 검증
    RefreshToken refreshToken = TokenValidator.extractAndValidateBearer(refreshTokenHeader);

    // Service를 통해 새 Access Token 발급
    AccessToken accessToken = regenerateAccessTokenService.regenerateAccessToken(refreshToken);

    // Access Token 반환
    return ResponseEntity.ok(accessToken);
  }
}