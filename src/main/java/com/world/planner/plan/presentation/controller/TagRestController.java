package com.world.planner.plan.presentation.controller;
import com.world.planner.plan.application.TagService;
import com.world.planner.plan.presentation.dto.request.TagRequest;
import com.world.planner.plan.presentation.dto.response.TagResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/tags", produces = "application/json; charset=UTF-8")
@RequiredArgsConstructor
@Tag(name = "Tag API", description = "Tag 관련 API를 제공합니다.")
public class TagRestController {

    private final TagService tagService;

    /**
     * Plan에 태그 추가
     *
     * @param planId Plan의 ID
     * @param request 태그 요청 (tagName 포함)
     * @return HTTP 상태코드 201 (Created)
     */
    @PostMapping("/{planId}")
    @Operation(summary = "Plan에 태그 추가", description = "특정 Plan에 태그를 추가합니다.")
    public ResponseEntity<Void> addTagToPlan(@PathVariable UUID planId, @RequestBody TagRequest request) {
        tagService.addTagToPlan(planId, request.getTagName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Plan에서 태그 제거
     *
     * @param planId Plan의 ID
     * @param request 태그 요청 (tagName 포함)
     * @return HTTP 상태코드 204 (No Content)
     */
    @DeleteMapping("/{planId}")
    @Operation(summary = "Plan에서 태그 제거", description = "특정 Plan에서 태그를 제거합니다.")
    public ResponseEntity<Void> removeTagFromPlan(@PathVariable UUID planId, @RequestBody TagRequest request) {
        tagService.removeTagFromPlan(planId, request.getTagName());
        return ResponseEntity.noContent().build();
    }

    /**
     * Plan에 연결된 태그 조회
     *
     * @param planId Plan의 ID
     * @return Plan에 연결된 태그 리스트
     */
    @GetMapping("/{planId}")
    @Operation(summary = "Plan의 태그 조회", description = "특정 Plan에 연결된 태그를 조회합니다.")
    public ResponseEntity<List<TagResponse>> getTagsForPlan(@PathVariable UUID planId) {
        List<String> tags = tagService.getTagsForPlan(planId);
        List<TagResponse> response = tags.stream()
                .map(TagResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * 태그로 연결된 Plan 조회
     *
     * @param tagName 검색 태그의 이름
     * @return 해당 태그와 연결된 Plan 리스트
     */
    @GetMapping("/plans/{tagName}")
    @Operation(summary = "태그로 Plan 조회", description = "특정 태그와 연결된 계획(Plan) 목록을 조회합니다.")
    public ResponseEntity<List<UUID>> getPlansByTag(@PathVariable String tagName) {
        List<UUID> plans = tagService.getPlansByTag(tagName);
        return ResponseEntity.ok(plans);
    }
}
