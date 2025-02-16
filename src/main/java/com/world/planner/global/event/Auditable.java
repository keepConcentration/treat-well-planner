package com.world.planner.global.event;

public interface Auditable<MemberId, Timestamp> {

  MemberId getCreatedBy();
  Timestamp getCreatedAt();
  MemberId getUpdatedBy();
  Timestamp getUpdatedAt();

  void setCreatedBy(MemberId createdBy);
  void setCreatedAt(Timestamp createdAt);
  void setUpdatedBy(MemberId updatedBy);
  void setUpdatedAt(Timestamp updatedAt);
}
