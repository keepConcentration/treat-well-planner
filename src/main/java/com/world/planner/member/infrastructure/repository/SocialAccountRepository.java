package com.world.planner.member.infrastructure.repository;

import com.world.planner.member.domain.SocialAccount;
import com.world.planner.member.domain.SocialProvider;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, UUID> {

  Optional<SocialAccount> findBySocialIdAndProvider(String socialId, SocialProvider provider);
}
