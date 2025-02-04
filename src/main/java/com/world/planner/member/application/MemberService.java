package com.world.planner.member.application;

import com.world.planner.global.utility.AuthenticationUtil;
import com.world.planner.member.domain.Member;
import com.world.planner.member.domain.SocialAccount;
import com.world.planner.member.domain.SocialProvider;
import com.world.planner.member.infrastructure.repository.MemberRepository;
import com.world.planner.member.infrastructure.repository.SocialAccountRepository;
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
  private final SocialAccountRepository socialAccountRepository;

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
      member.updateMemberName(request.getName());
    }
    memberRepository.save(member); // 변경사항 저장
  }

  /**
   * 소셜 계정을 통해 회원을 조회하거나 새롭게 생성합니다.
   *
   * @param socialAccountId 소셜 계정 고유 ID
   * @param socialProvider 소셜 제공사 (KAKAO, GOOGLE 등)
   * @return 회원 고유 ID
   */
  public UUID findOrCreateMemberBySocialAccount(String socialAccountId, SocialProvider socialProvider) {
    return socialAccountRepository.findBySocialIdAndProvider(socialAccountId, socialProvider)
        .map(SocialAccount::getMember)
        .map(Member::getId)
        .orElseGet(() -> {
          // 새로운 사용자(Member) 생성
          Member newMember = Member.create();
          memberRepository.save(newMember);

          // 새로운 소셜 계정 생성 및 연결
          SocialAccount newSocialAccount = SocialAccount.create(socialAccountId, socialProvider);
          newMember.addSocialAccount(newSocialAccount);

          return newMember.getId();
        });
  }
}
