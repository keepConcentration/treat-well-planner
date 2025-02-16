package com.world.planner.auth.presentation.dto.response;

import com.world.planner.auth.domain.token.AccessToken;
import com.world.planner.auth.domain.token.RefreshToken;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "JWT 응답 객체")
public class JwtResponse {

  @Schema(description = "Access Token")
  private AccessToken accessToken;

  @Schema(description = "Refresh Token")
  private RefreshToken refreshToken;

}