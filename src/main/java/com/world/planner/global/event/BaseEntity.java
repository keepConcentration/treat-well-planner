package com.world.planner.global.event;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
public class BaseEntity implements Auditable {

  @CreatedBy
  @Column(name = "insert_id", nullable = false, updatable = false)
  private String createdBy;

  @CreatedDate
  @Column(name = "insert_date_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedBy
  @Column(name = "update_id", nullable = false)
  private String updatedBy;

  @LastModifiedDate
  @Column(name = "update_date_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  public void prePersist() {
    if (createdBy == null) {
      this.createdBy = "system";
    }
    if (updatedBy == null) {
      this.updatedBy = "system";
    }
    if (createdAt == null) {
      this.createdAt = LocalDateTime.now(); // 현재 시간을 기본값으로 설정
    }
    if (updatedAt == null) {
      this.updatedAt = LocalDateTime.now(); // 현재 시간을 기본값으로 설정
    }
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now(); // 업데이트 타임 스탬프 설정
  }

}
