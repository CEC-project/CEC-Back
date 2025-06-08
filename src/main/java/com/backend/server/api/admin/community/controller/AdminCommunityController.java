package com.backend.server.api.admin.community.controller;

import com.backend.server.api.admin.community.dto.AdminCommunityListRequest;
import com.backend.server.api.admin.community.dto.AdminCommunityListResponse;
import com.backend.server.api.admin.community.dto.AdminCommunityResponse;
import com.backend.server.api.admin.community.service.AdminCommunityService;
import com.backend.server.api.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@Tag(name = "5-2. 게시판 관리 / 게시글")
@RestController
@RequestMapping("/api/admin/board")
@RequiredArgsConstructor
public class AdminCommunityController {

    private final AdminCommunityService adminCommunityService;

    @Operation(summary = "사용자 게시글 목록 조회")
    @GetMapping
    public ApiResponse<AdminCommunityListResponse> getCommunities(@ParameterObject AdminCommunityListRequest request) {
        return ApiResponse.success("사용자 게시글 목록 조회 성공", adminCommunityService.getCommunityPosts(request));
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 조회.")
    @GetMapping("/{id}")
    public ApiResponse<AdminCommunityResponse> getCommunity(@PathVariable Long id) {
        return ApiResponse.success("사용자 게시글 조회 성공", adminCommunityService.getCommunityPost(id));
    }

    @Operation(summary = "사용자 게시글 삭제")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCommunity(@PathVariable Long id) {
        adminCommunityService.deleteCommunityPost(id);
        return ApiResponse.success("사용자 게시글 삭제 성공", null);
    }

}
