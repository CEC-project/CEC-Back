package com.backend.server.api.user.community.controller;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.community.dto.CommunityListResponse;
import com.backend.server.api.user.community.dto.CommunityResponse;
import com.backend.server.api.user.community.dto.CreatePostRequest;
import com.backend.server.api.user.community.dto.UpdatePostRequest;
import com.backend.server.api.user.community.service.CommunityService;
import com.backend.server.model.entity.Community;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "2. 게시판", description = "수정 1차 완")
@RestController
@RequestMapping("/api/user/community")
public class CommunityController {

    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    // 게시글 목록 조회 엔드포인트 수정: typeId 요청 파라미터 추가
    // GET /api/user/community?page=0&size=10&sort=createdAt,desc&typeId=1 (예: typeId=1은 '자유' 게시글)
    // GET /api/user/community?page=0&size=10&sort=createdAt,desc (typeId 없으면 전체 게시글)
    @GetMapping
    @Operation(summary = "게시판 목록 조회")
    public ResponseEntity<CommunityListResponse> getCommunityPosts(
        @PageableDefault(size = 10, sort = "createdAt") Pageable pageable,
        @RequestParam(required = false) Long typeId, // <-- typeId 요청 파라미터 추가 (필수 아님)
        @AuthenticationPrincipal LoginUser loginuser // 실제 환경에서 로그인 사용자 정보 주입 필요
    ) {
        // Service 레이어 호출 시 typeId 인자를 함께 전달합니다.
        CommunityListResponse response = communityService.getPosts(pageable, loginuser, typeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "게시판 단일 조회")
    public ResponseEntity<CommunityResponse> getCommunityPostById(
        @PathVariable Long id,
        @AuthenticationPrincipal LoginUser loginuser
    ) {
        CommunityResponse response = communityService.getPostById(id, loginuser);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "게시판 글 쓰기")
    public ResponseEntity<CommunityResponse> createCommunityPost(
        @RequestBody CreatePostRequest request,
        @AuthenticationPrincipal LoginUser loginuser
    ) {
        CommunityResponse response = communityService.createPost(request, loginuser.getId(), loginuser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "게시판 글 수정")
    public ResponseEntity<CommunityResponse> updateCommunityPost(
        @PathVariable Long id,
        @RequestBody UpdatePostRequest request,
        @AuthenticationPrincipal LoginUser loginuser
    ) {
        Community communityDetailsToUpdate = Community.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .type(request.getType())
            .typeId(request.getCommunityTypeId())
            .build();

        CommunityResponse response = communityService.updatePost(id, communityDetailsToUpdate, loginuser);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "게시판 글 삭제")
    public ResponseEntity<Void> deleteCommunityPost(
        @PathVariable Long id,
        @AuthenticationPrincipal LoginUser loginuser
    ) {
        communityService.deletePost(id, loginuser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/recommend")
    @Operation(summary = "게시판 글 따1봉")
    public ResponseEntity<CommunityResponse> recommendCommunityPost(
        @PathVariable Long id,
        @AuthenticationPrincipal LoginUser loginuser
    ) {
        CommunityResponse response = communityService.recommendPost(id, loginuser);
        return ResponseEntity.ok(response);
    }
}
