package com.world.planner.global.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import lombok.AllArgsConstructor;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // 유효성 검증 실패 처리
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldError().getDefaultMessage();
    return new ResponseEntity<>(
        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation Error", message),
        HttpStatus.BAD_REQUEST
    );
  }

  // 기타 예외 처리
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
    return new ResponseEntity<>(
        new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", ex.getMessage()),
        HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  @Getter
  @AllArgsConstructor
  static class ErrorResponse {
    private int status; // HTTP 상태 코드
    private String error; // 에러 유형
    private String message; // 에러 메시지
  }
}
