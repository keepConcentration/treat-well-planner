package com.world.planner.global.event;

import java.time.LocalDateTime;

public interface Auditable {

  String getCreatedBy();

  LocalDateTime getCreatedAt();

  String getUpdatedBy();

  LocalDateTime getUpdatedAt();

  void setCreatedBy(String createdBy);

  void setCreatedAt(LocalDateTime createdAt);

  void setUpdatedBy(String updatedBy);

  void setUpdatedAt(LocalDateTime updatedAt);
}
