package com.world.planner.auth.presentation;

import com.world.planner.auth.domain.token.RefreshToken;
import com.world.planner.auth.utility.TokenValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Token Validation", description = "JWT 토큰 유효성 검사 API")
@RestController
@RequestMapping("/api/auth/validate-token")
@RequiredArgsConstructor
public class TokenValidationRestController {

  @Operation(
      summary = "JWT 토큰 유효성 검사",
      description = "클라이언트가 전달한 JWT 토큰의 유효성을 검사합니다. 유효하면 204 응답, 유효하지 않으면 401 에러를 반환합니다.",
      responses = {
          @ApiResponse(responseCode = "204", description = "토큰이 유효함"),
          @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰 (만료 또는 검증 실패)")
      }
  )
  @GetMapping
  public ResponseEntity<Void> validateToken(
      @RequestHeader("Authorization") String authorizationHeader) {
    RefreshToken refreshToken = TokenValidator.extractAndValidateBearer(authorizationHeader);

    TokenValidator.validateRefreshToken(refreshToken);

    return ResponseEntity.noContent().build();
  }
}