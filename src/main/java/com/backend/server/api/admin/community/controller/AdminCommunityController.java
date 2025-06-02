package com.backend.server.api.admin.community.controller;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.community.dto.CommunityListResponse;
import com.backend.server.api.user.community.dto.CommunityResponse;
import com.backend.server.api.user.community.dto.UpdatePostRequest;
import com.backend.server.api.user.community.service.CommunityService;
import com.backend.server.model.entity.Community;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// Spring Security 사용 시 관리자 권한 체크를 위한 어노테이션 또는 설정 필요
// @PreAuthorize("hasRole('ADMIN')") // 예시: 메소드 실행 전 ADMIN 역할 확인

@Tag(name = "5-2. 게시판 관리 / 게시글", description = "수정 필요")
@RestController
@RequestMapping("/api/admin/community") // 관리자용 기본 URL 경로
public class AdminCommunityController {

    private final CommunityService communityService; // CommunityService 사용

    // 생성자 주입
    public AdminCommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    // 관리자용 게시글 목록 조회 (전체 게시글 또는 타입별)
    // GET /api/admin/community?page=0&size=10&typeId=...
    @GetMapping
    // @PreAuthorize("hasRole('ADMIN')") // 이 메소드는 ADMIN만 접근 가능하도록 설정 가능
    public ResponseEntity<CommunityListResponse> getCommunityPostsForAdmin(
        @PageableDefault(size = 20, sort = "createdAt") Pageable pageable, // 관리자는 더 많은 항목 조회 가능하도록 기본값 조정 등
        @RequestParam(required = false) Long typeId, // 타입별 필터링 가능
        @AuthenticationPrincipal LoginUser adminUser // 실제 환경에서 관리자 정보 주입 필요
    ) {
        // Service 레이어의 게시글 목록 조회 메소드를 호출합니다.
        // 이 메소드는 사용자용과 동일하지만, Admin UI에서 호출된다고 가정합니다.
        // CommunityResponse DTO 생성 시 LoginUser 정보를 넘겨주면 Admin 여부에 따라 authorName 표시 방식이 달라집니다.
        CommunityListResponse response = communityService.getPosts(pageable, adminUser, typeId); // typeId 필터링 사용 가능
        return ResponseEntity.ok(response);
    }

    // 관리자용 단일 게시글 상세 조회
    // GET /api/admin/community/{id}
    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 조회.")
    @GetMapping("/{id}")
    public ResponseEntity<CommunityResponse> getCommunityPostByIdForAdmin(
        @PathVariable Long id,
        @AuthenticationPrincipal LoginUser adminUser // 실제 환경에서 관리자 정보 주입 필요
    ) {
        // Service 레이어의 단일 게시글 조회 메소드를 호출합니다.
        // 조회수 증가 로직 포함 (관리자 조회 시 조회수 증가 정책은 결정 필요)
        // DTO 변환 시 Admin LoginUser 정보를 넘겨 authorName 표시를 결정합니다.
        CommunityResponse response = communityService.getPostById(id, adminUser);
        return ResponseEntity.ok(response);
    }

    // 관리자용 게시글 수정 엔드포인트
    // PUT /api/admin/community/{id}
    @Operation(summary = "게시글 수정", description = "게시글 수정.")
    @PutMapping("/{id}")
    public ResponseEntity<CommunityResponse> updateCommunityPostByAdmin(
        @PathVariable Long id,
        @RequestBody UpdatePostRequest request// 사용자용과 동일한 요청 DTO 사용
    ) {
        // UpdatePostRequest -> Community 엔티티로 변환
        Community communityDetailsToUpdate = Community.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .type(request.getType())
            .typeId(request.getCommunityTypeId())
            .build();

        // Service 레이어의 관리자용 수정 메소드를 호출합니다. (작성자 권한 체크 없음)
        // 반환되는 DTO 생성 시 Admin LoginUser 정보가 필요할 수 있습니다.
        CommunityResponse response = communityService.adminUpdatePost(id, communityDetailsToUpdate); // 여기서는 adminUpdatePost 호출

        return ResponseEntity.ok(response);
    }

    // 관리자용 게시글 삭제 엔드포인트
    // DELETE /api/admin/community/{id}
    @Operation(summary = "게시글 삭제", description = "게시글 삭제.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunityPostByAdmin(@PathVariable Long id) {
        // Service 레이어의 관리자용 삭제 메소드를 호출합니다. (작성자 권한 체크 없음)
        communityService.adminDeletePost(id); // 여기서는 adminDeletePost 호출

        return ResponseEntity.noContent().build(); // 삭제 성공 시 204 No Content 반환
    }

    // TODO: 관리자 페이지에서 필요한 예외 처리 로직 추가 (사용자 페이지와 동일하거나 다르게 처리 가능)
}
