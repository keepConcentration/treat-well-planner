package com.world.planner.global.event;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor
@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity implements Auditable<UUID, LocalDateTime> {

  @CreatedBy
  @Column(name = "insert_id", nullable = false, updatable = false)
  @NotNull(message = "Creator ID cannot be null")
  private UUID createdBy;

  @CreatedDate
  @Column(name = "insert_date_time", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedBy
  @Column(name = "update_id", nullable = false)
  @NotNull(message = "Updater ID cannot be null")
  private UUID updatedBy;

  @LastModifiedDate
  @Column(name = "update_date_time", nullable = false)
  private LocalDateTime updatedAt;

}
