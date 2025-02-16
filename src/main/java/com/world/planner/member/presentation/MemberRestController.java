package com.world.planner.member.presentation;

import com.world.planner.member.application.MemberService;
import com.world.planner.member.presentation.dto.request.UpdateMemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST 컨트롤러로 회원 관련 API를 제공한다.
 */
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Member API", description = "Member 관련 API")
public class MemberRestController {

  private final MemberService memberService;

  /**
   * 회원 탈퇴 API
   *
   * @param id 탈퇴할 회원의 UUID
   * @return 성공 시 204 상태 코드 반환
   */
  @Operation(
      summary = "회원 탈퇴",
      description = "특정 회원을 UUID를 통해 삭제합니다. 삭제된 회원은 더 이상 접근이 불가합니다.",
      tags = {"Member API"}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "회원 탈퇴 성공", content = @Content),
      @ApiResponse(responseCode = "404", description = "조회하려는 회원을 찾을 수 없습니다.", content = @Content)
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMember(@PathVariable UUID id) {
    memberService.deleteMember(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * 회원 로그아웃 API
   *
   * @return 성공 시 200 상태 코드 반환
   */
  @Operation(
      summary = "회원 로그아웃",
      description = "현재 로그인된 사용자의 JWT를 삭제하고 로그아웃합니다.",
      tags = {"Member API"}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content)
  })
  @PostMapping("/logout")
  public ResponseEntity<Void> logout() {
    // 클라이언트에서 저장된 JWT를 삭제하는 요청
    return ResponseEntity.ok().build();
  }

  /**
   * 회원 정보 수정 API
   *
   * @param id 수정할 회원의 UUID
   * @param request 수정할 정보(name 등)
   * @return 성공 시 200 상태 코드 반환
   */
  @Operation(
      summary = "회원 정보 수정",
      description = "회원의 이름 등 정보를 수정합니다. 요청 본문에서 수정할 정보를 제공합니다.",
      tags = {"Member API"}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공", content = @Content),
      @ApiResponse(responseCode = "404", description = "수정하려는 회원을 찾을 수 없습니다.", content = @Content)
  })
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "회원 정보 수정 요청. 수정할 이름을 전달합니다.",
      required = true,
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateMemberRequest.class))
  )
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateMember(@PathVariable UUID id, @RequestBody UpdateMemberRequest request) {
    memberService.updateMember(id, request);
    return ResponseEntity.ok().build();
  }
}