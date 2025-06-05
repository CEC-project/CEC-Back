package com.backend.server.api.user.comment.controller;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.comment.dto.CommentListRequest;
import com.backend.server.api.user.comment.dto.CommentListResponse;
import com.backend.server.api.user.comment.dto.CommentRequest;
import com.backend.server.api.user.comment.dto.CommentUpdateRequest;
import com.backend.server.api.user.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "2-4. 게시판 / 댓글", description = "수정 1차 완")
@RestController
@RequestMapping("/api/users/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(
        summary = "댓글 작성"
    )
    @PostMapping
    public ApiResponse<Long> createComment(
        @Parameter(description = "댓글 생성 요청 DTO") @RequestBody CommentRequest request,
        @Parameter(description = "로그인 유저 정보") @AuthenticationPrincipal LoginUser loginUser) {
        return ApiResponse.success("댓글 작성 성공", commentService.createComment(request, loginUser));
    }

    @Operation(
        summary = "댓글 목록 조회"
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
