package com.world.planner.plan.application;

import com.world.planner.plan.domain.Plan;
import com.world.planner.plan.domain.Tag;
import com.world.planner.plan.infrastructure.repository.PlanRepository;
import com.world.planner.plan.infrastructure.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

    private final TagRepository tagRepository;
    private final PlanRepository planRepository;

    /**
     * 태그 추가 (Plan에 태그를 연결)
     *
     * @param planId Plan의 ID
     * @param tagName 태그 이름
     */
    public void addTagToPlan(UUID planId, String tagName) {
        Plan plan = findPlanById(planId);

        // 태그 찾기 or 생성
        Tag tag = tagRepository.findByName(tagName)
                .orElseGet(() -> tagRepository.save(Tag.create(tagName)));

        // Plan에 태그 추가
        plan.addTag(tag);
        planRepository.save(plan);
    }

    /**
     * Plan에서 태그 제거
     *
     * @param planId Plan의 ID
     * @param tagName 태그 이름
     */
    public void removeTagFromPlan(UUID planId, String tagName) {
        Plan plan = findPlanById(planId);
        Tag tag = tagRepository.findByName(tagName)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with name: " + tagName));

        // Plan에서 태그 제거
        plan.removeTag(tag);
        planRepository.save(plan);
    }

    /**
     * 특정 Plan의 태그 목록 조회
     *
     * @param planId Plan의 ID
     * @return 태그 이름 목록
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<String> getTagsForPlan(UUID planId) {
        Plan plan = findPlanById(planId);
        return plan.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
    }

    /**
     * 특정 태그에 연결된 Plan 목록 조회
     *
     * @param tagName 태그 이름
     * @return 연결된 Plan ID 목록
     */
    @Transactional(readOnly = true)
    public List<UUID> getPlansByTag(String tagName) {
        Tag tag = tagRepository.findByName(tagName)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with name: " + tagName));

        return tag.getPlans().stream()
                .map(Plan::getId)
                .collect(Collectors.toList());
    }

    // Helper 메서드: Plan 찾기
    private Plan findPlanById(UUID planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found with ID: " + planId));
    }

}
