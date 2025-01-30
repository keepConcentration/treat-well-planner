package com.world.planner.member.application;

import com.world.planner.global.config.JwtTokenProvider;
import com.world.planner.global.util.AuthenticationUtil;
import com.world.planner.member.domain.Member;
import com.world.planner.member.infrastructure.repository.MemberRepository;
import com.world.planner.member.presentation.dto.request.UpdateMemberRequest;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final AuthenticationUtil authenticationUtil;
  private final JwtTokenProvider jwtTokenProvider;

  /**
   * 소셜 로그인 처리 로직
   *
   * @param email 소셜 인증에서 가져온 이메일
   * @param name 소셜 인증에서 가져온 사용자 이름
   * @return JWT 토큰
   */
  public String socialLogin(String email, String name) {
    // 이메일을 기준으로 회원 정보 조회, 없으면 자동 회원가입
    Member member = memberRepository.findByEmail(email).orElseGet(() -> {
      // 새로운 회원 생성
      Member newMember = Member.create(email, name);
      return memberRepository.save(newMember);
    });

    // 해당 회원 정보로 JWT 토큰 발급
    return jwtTokenProvider.createToken(member.getId());
  }

  /**
   * 현재 인증된 사용자 가져오기
   * @return 현재 인증된 Member
   */
  public Member getAuthenticatedMember() {
    String authenticatedEmail = authenticationUtil.getAuthenticatedEmail();
    return memberRepository.findByEmail(authenticatedEmail)
        .orElseThrow(() -> new EntityNotFoundException("Member not found with email: " + authenticatedEmail));
  }

  public void deleteMember(UUID id) {
    Member member = memberRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + id));

    memberRepository.delete(member);
  }

  public void updateMember(UUID memberId, UpdateMemberRequest request) {
    // 회원 조회
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));

    // 요청 확인 및 업데이트
    if (request.getName() != null) {
      member.updateMemberInfo(request.getName());
    }
    memberRepository.save(member); // 변경사항 저장
  }
}
