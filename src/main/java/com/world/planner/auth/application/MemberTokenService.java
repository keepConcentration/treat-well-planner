package com.world.planner.auth.application;

import com.world.planner.auth.domain.MemberToken;
import com.world.planner.auth.domain.token.AccessToken;
import com.world.planner.auth.domain.token.RefreshToken;
import com.world.planner.auth.infrastructure.repository.MemberTokenRepository;
import com.world.planner.auth.presentation.dto.response.JwtResponse;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberTokenService {

  private final MemberTokenRepository memberTokenRepository;

  /**
   * AccessToken과 RefreshToken을 생성하여 저장합니다.
   *
   * @param memberId 회원 ID
   * @return 생성된 AccessToken 및 RefreshToken 포함 응답
   */
  @Transactional
  public JwtResponse createAndStoreJwts(UUID memberId) {
    RefreshToken refreshToken = RefreshToken.create(memberId);
    LocalDateTime expiration = refreshToken.getExpiration();

    MemberToken memberToken = memberTokenRepository.findById(memberId)
        .map(existingToken -> {
          existingToken.setRefreshToken(refreshToken.getValue());
          existingToken.setExpiration(expiration);
          return existingToken;
        })
        .orElse(MemberToken.builder()
            .memberId(memberId)
            .refreshToken(refreshToken.getValue())
            .expiration(expiration)
            .build());

    // Refresh Token 저장
    memberTokenRepository.save(memberToken);

    AccessToken accessToken = AccessToken.create(memberId);

    // JwtTokenResponse로 반환
    return JwtResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }
}
