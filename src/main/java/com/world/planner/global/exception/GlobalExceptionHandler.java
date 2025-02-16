package com.world.planner.global.exception;

import com.world.planner.global.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 유효성 검증 실패 예외를 처리합니다.
   *
   * @param ex 유효성 검증 실패로 인해 발생한 예외
   * @return 상세한 검증 오류 메시지가 포함된 응답 객체
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldError() != null
        ? ex.getBindingResult().getFieldError().getDefaultMessage()
        : "유효성 검증 오류";
    return ResponseEntity.badRequest().body(
        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "유효성 검증 실패", message)
    );
  }

  /**
   * 엔티티를 찾을 수 없을 때 발생하는 예외를 처리합니다.
   *
   * @param ex 엔티티를 찾을 수 없어 발생한 예외
   * @return '엔티티를 찾을 수 없음' 오류 정보가 포함된 응답 객체
   */
  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        new ErrorResponse(HttpStatus.NOT_FOUND.value(), "엔티티를 찾을 수 없음", ex.getMessage())
    );
  }

  /**
   * 인증 및 인가가 실패했을 때 발생하는 예외를 처리합니다.
   *
   * @param ex 인증 또는 인가 실패로 인해 발생한 예외
   * @return '접근 거부' 오류 정보가 포함된 응답 객체
   */
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
        new ErrorResponse(HttpStatus.FORBIDDEN.value(), "접근 거부", "접근이 거부되었습니다.")
    );
  }

  /**
   * 유효하지 않은 JWT 토큰 예외를 처리합니다.
   *
   * @param ex 유효하지 않은 JWT 토큰으로 인해 발생한 예외
   * @return '유효하지 않은 JWT 토큰' 오류 정보가 포함된 응답 객체
   */
  @ExceptionHandler(InvalidJwtTokenException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<ErrorResponse> handleInvalidJwtTokenException(InvalidJwtTokenException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        new ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            "유효하지 않은 JWT 토큰",
            ex.getMessage()
        )
    );
  }

  /**
   * 런타임 예외를 처리합니다.
   *
   * @param ex 예상치 못한 런타임 문제로 인해 발생한 예외
   * @return '서버 내부 오류' 상세 정보가 포함된 응답 객체
   */
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류", ex.getMessage())
    );
  }

  /**
   * 처리되지 않은 기타 예외를 처리합니다.
   *
   * @param ex 예기치 못한 문제로 인해 발생한 예외
   * @return '예기치 못한 오류' 상세 정보가 포함된 응답 객체
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "예기치 못한 오류", ex.getMessage())
    );
  }
}