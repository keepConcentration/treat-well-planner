package com.world.planner.auth.domain;

import com.world.planner.global.event.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberToken extends BaseEntity {

  @Id
  @Column(name = "member_id", nullable = false, unique = true)
  private UUID memberId; // 사용자 고유 ID

  @Column(name = "refresh_token", nullable = false, length = 1000)
  private String refreshToken; // Refresh Token 저장

  @Column(name = "expiration", nullable = false)
  private LocalDateTime expiration; // 토큰 만료 시간

}