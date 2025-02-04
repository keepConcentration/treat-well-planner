package com.world.planner.member.domain;
import com.world.planner.global.event.BaseEntity;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "social_accounts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialAccount extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String socialId; // 소셜 제공사의 고유 사용자 ID

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SocialProvider provider; // 소셜 제공사 (카카오, 구글 등)

  @Setter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  private SocialAccount(String socialId, SocialProvider provider) {
    this.socialId = socialId;
    this.provider = provider;
  }

  public static SocialAccount create(String socialId, SocialProvider provider) {
    if (socialId == null || socialId.isBlank()) {
      throw new IllegalArgumentException("Social ID cannot be blank or null.");
    }
    return new SocialAccount(socialId, provider);
  }
}
