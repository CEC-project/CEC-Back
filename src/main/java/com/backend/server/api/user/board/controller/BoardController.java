package com.backend.server.api.user.board.controller;

import com.backend.server.api.admin.community.dto.CommunityListRequest;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.board.dto.BoardCategoryListResponse;
import com.backend.server.api.user.board.dto.BoardListResponse;
import com.backend.server.api.user.board.dto.BoardResponse;
import com.backend.server.api.user.board.dto.BoardPostRequest;
import com.backend.server.api.user.board.dto.UpdatePostRequest;
import com.backend.server.api.user.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "2. 게시판", description = "수정 1차 완")
@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 게시글 목록 조회 엔드포인트 수정: typeId 요청 파라미터 추가
    // GET /api/user/community?page=0&size=10&sort=createdAt,desc&typeId=1 (예: typeId=1은 '자유' 게시글)
    // GET /api/user/community?page=0&size=10&sort=createdAt,desc (typeId 없으면 전체 게시글)

    @GetMapping
    @Operation(summary = "게시판 카테고리 조회")
    public ApiResponse<List<BoardCategoryListResponse>> getBoardCategories() {
        List<BoardCategoryListResponse> categories = boardService.getBoardCategories();
        return ApiResponse.success("게시판 카테고리 내역입니다", categories);
    }

    @GetMapping("/post")
    @Operation(summary = "게시글 목록 조회")
    public ApiResponse<BoardListResponse> getCommunityPosts(
            @ParameterObject CommunityListRequest request,
            @AuthenticationPrincipal LoginUser loginuser // 실제 환경에서 로그인 사용자 정보 주입 필요
    ) {
        // Service 레이어 호출 시 typeId 인자를 함께 전달합니다.
        BoardListResponse response = boardService.getPosts(loginuser, request);
        return ApiResponse.success("게시글 목록 조회 성공", response);
    }

    @GetMapping("/post/{id}")
    @Operation(summary = "게시글 상세 보기")
    public ApiResponse<BoardResponse> getCommunityPostById(
        @PathVariable Long id,
        @AuthenticationPrincipal LoginUser loginuser
    ) {
        BoardResponse response = boardService.getPostById(id, loginuser);
        return ApiResponse.success("게시글 상세보기 성공", response);
    }

    @PostMapping("/post")
    @Operation(summary = "게시판 글 쓰기")
    public ApiResponse<Long> createCommunityPost(
        @RequestBody BoardPostRequest request,
        @AuthenticationPrincipal LoginUser loginuser
    ) {
        BoardResponse response = boardService.createPost(request, loginuser.getId(), loginuser);

        return ApiResponse.success("게시글 글쓰기 성공", response.getId());
    }

    @PutMapping("/post/{id}")
    @Operation(summary = "게시글 수정")
    public ApiResponse<Long> updateCommunityPost(
            @PathVariable Long id,
            @RequestBody @Valid UpdatePostRequest request,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        BoardResponse response = boardService.updatePost(id, request, loginUser);
        return ApiResponse.success("게시글 수정 성공", response.getId());
    }

    @DeleteMapping("/post/{id}")
    @Operation(summary = "게시글 삭제")
    public ApiResponse<Void> deleteCommunityPost(
        @PathVariable Long id,
        @AuthenticationPrincipal LoginUser loginuser
    ) {
        boardService.deletePost(id, loginuser);
        return ApiResponse.success("게시글 삭제 성공", null);
    }

    @PatchMapping("/post/{id}/recommend")
    @Operation(summary = "게시글 추천")
    public ApiResponse<Long> recommendCommunityPost(
        @PathVariable Long id,
        @AuthenticationPrincipal LoginUser loginuser
    ) {
        BoardResponse response = boardService.recommendPost(id, loginuser);
        return ApiResponse.success("게시글 추천 성공", response.getId());
    }

    @GetMapping("/post/{id}/recommend") // URL 경로: /api/board + /post/{id}
    @Operation(summary = "현재 로그인한 사용자가 추천했는지 여부 확인")
    public ApiResponse<Boolean> checkUserRecommendation(
            @PathVariable Long id, // 확인할 게시글 ID
            @AuthenticationPrincipal LoginUser loginuser // 현재 로그인한 사용자 정보
    ) {
        // 현재 로그인한 사용자의 ID를 가져옵니다.
        Long userId = loginuser.getId();

        // Service 레이어에 추천 여부 확인 로직 위임
        boolean isRecommended = boardService.hasUserRecommended(id, userId);

        // 추천 여부 결과 (boolean)를 200 OK 상태 코드와 함께 응답
        // Swagger 명세서의 ApiResponseBoolean 형식에 맞춰 DTO로 감싸서 반환해야 할 수도 있습니다.
        // 현재 Service는 boolean을 반환하므로 여기서는 boolean을 직접 반환합니다.
        return ApiResponse.success("추천 여부 조회 성공", isRecommended);
    }
}
