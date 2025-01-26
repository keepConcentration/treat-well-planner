package com.world.planner.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

  private int status; // HTTP 상태 코드
  private String error; // 에러 유형
  private String message; // 에러 메시지
}
