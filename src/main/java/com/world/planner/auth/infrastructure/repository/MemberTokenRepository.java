package com.world.planner.auth.infrastructure.repository;

import com.world.planner.auth.domain.MemberToken;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTokenRepository extends JpaRepository<MemberToken, UUID> {
  Optional<MemberToken> findByMemberId(UUID memberId);
}
