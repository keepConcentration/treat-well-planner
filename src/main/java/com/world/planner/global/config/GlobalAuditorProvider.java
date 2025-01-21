package com.world.planner.global.config;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component("auditorAware")
public class GlobalAuditorProvider implements AuditorAware<UUID> {

  @Override
  public Optional<UUID> getCurrentAuditor() {
    return Optional.of(UUID.fromString("00000000-0000-0000-0000-000000000001"));
  }
}
