package com.world.planner.global.event;

public interface Auditable<UserID, Timestamp> {

  UserID getCreatedBy();
  Timestamp getCreatedAt();
  UserID getUpdatedBy();
  Timestamp getUpdatedAt();

  void setCreatedBy(UserID createdBy);
  void setCreatedAt(Timestamp createdAt);
  void setUpdatedBy(UserID updatedBy);
  void setUpdatedAt(Timestamp updatedAt);
}
