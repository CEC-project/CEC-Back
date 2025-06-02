package com.backend.server.api.user.comment.controller;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.comment.dto.*;
import com.backend.server.api.user.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "2. 게시판 / 댓글", description = "수정 필요")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "댓글 작성",
            description = """
                새로운 댓글을 작성합니다.
                
                **댓글 타입:**
                - NOTICE: 공지사항 댓글
                - BOARD: 게시판 댓글
                - INQUIRY: 문의 댓글
                
                **대댓글 작성:**
                - parentCommentId에 부모 댓글 ID를 넣으면 대댓글로 작성됩니다.
                
                **예시 URL:** http://localhost:8080/api/comments
                """
    )
    @PostMapping
    public ApiResponse<Long> createComment(
            @Parameter(description = "댓글 생성 요청 DTO") @RequestBody CommentRequest request,
            @Parameter(description = "로그인 유저 정보") @AuthenticationPrincipal LoginUser loginUser) {
        return ApiResponse.success("댓글 작성 성공", commentService.createComment(request, loginUser));
    }

    @Operation(
            summary = "댓글 목록 조회",
            description = """
                특정 대상(공지사항, 게시글 등)의 댓글 목록을 조회합니다.
                
                **필수 파라미터:**
                - type: 댓글 대상 타입 (NOTICE, BOARD, INQUIRY)
                - targetId: 댓글 대상 ID
                
                **페이징 파라미터:**
                - page: 페이지 번호 (0부터 시작, 기본값: 0)
                - size: 한 페이지에 보여줄 개수 (기본값: 10)
                - sortBy: 정렬 기준 (ID, 기본값: ID)
                - direction: 정렬 방향 (ASC, DESC, 기본값: ASC)
                
                **예시 URL:**
                - 기본 조회: http://localhost:8080/api/comments?type=NOTICE&targetId=1
                - 페이징 조회: http://localhost:8080/api/comments?type=NOTICE&targetId=1&page=0&size=10
                - 정렬 조회: http://localhost:8080/api/comments?type=NOTICE&targetId=1&sortBy=ID&direction=DESC
                """
    )
    @GetMapping
    public ApiResponse<CommentListResponse> getComments(
            @Parameter(description = "댓글 목록 조회 Parameter") @ParameterObject CommentListRequest request) {
        return ApiResponse.success("댓글 목록 조회 성공", commentService.getComments(request));
    }

    @Operation(
            summary = "댓글 수정",
            description = """
                기존 댓글의 내용을 수정합니다.
                
                **권한:**
                - 본인이 작성한 댓글만 수정 가능합니다.
                
                **예시 URL:** http://localhost:8080/api/comments/1
                """
    )
    @PatchMapping("{id}")
    public ApiResponse<Long> updateComment(
            @Parameter(description = "댓글 수정 요청 DTO") @RequestBody CommentUpdateRequest request,
            @Parameter(description = "댓글 ID") @PathVariable("id") Long commentId,
            @AuthenticationPrincipal LoginUser loginUser) {
        return ApiResponse.success("댓글 수정 성공", commentService.updateComment(request, commentId, loginUser));
    }

    @Operation(
            summary = "댓글 삭제",
            description = """
                댓글을 삭제합니다.
                
                **권한 검증:**
                - 본인이 작성한 댓글만 삭제 가능합니다.
                - 권한이 없는 경우 AccessDeniedException이 발생합니다.
                
                **삭제 방식:**
                - 현재는 물리적 삭제(완전 삭제)를 수행합니다.
                - 대댓글이 있는 경우에도 완전 삭제됩니다.
                
                **주의사항:**
                - 삭제된 댓글은 복구할 수 없습니다.
                - 대댓글이 있는 댓글을 삭제하면 대댓글도 고아 상태가 될 수 있습니다.
                
                **예시 URL:** http://localhost:8080/api/comments/1
                
                **응답:**
                - 삭제된 댓글의 ID를 반환합니다.
                """
    )
    @DeleteMapping("{id}")
    public ApiResponse<Long> deleteComment(
            @Parameter(description = "댓글 ID") @PathVariable("id") Long commentId,
            @AuthenticationPrincipal LoginUser loginUser) {
        return ApiResponse.success("댓글 삭제 성공", commentService.deleteComment(commentId, loginUser));
    }
}
