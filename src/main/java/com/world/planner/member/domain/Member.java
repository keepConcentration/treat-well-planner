package com.world.planner.member.domain;

import com.world.planner.global.event.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String name;

  public void updateMemberInfo(String name) {
    validateName(name);
    this.name = name;
  }

  private Member(String email, String name) {
    this.email = email;
    this.name = name;
  }

  public static Member create(String email, String name) {
    validateEmail(email); // 유효성 검증
    validateName(name);
    return new Member(email, name);
  }

  // 이메일 검증 로직 (유효성 체크)
  private static void validateEmail(String email) {
    if (email == null || !email.contains("@")) {
      throw new IllegalArgumentException("Invalid email: " + email);
    }
  }

  // 이름 검증 로직
  private static void validateName(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be blank or null.");
    }
  }

}
