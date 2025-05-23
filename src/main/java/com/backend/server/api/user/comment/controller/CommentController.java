package com.backend.server.api.user.comment.controller;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.comment.dto.CommentIdResponse;
import com.backend.server.api.user.comment.dto.CommentListRequest;
import com.backend.server.api.user.comment.dto.CommentListResponse;
import com.backend.server.api.user.comment.dto.CommentRequest;
import com.backend.server.api.user.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "댓글", description = "댓글 API")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "댓글 작성",
            description = "댓글 작성 api"
    )
    @PostMapping
    public ApiResponse<CommentIdResponse> createComment(
            @Parameter(description = "댓글 생성 요청 DTO") @RequestBody CommentRequest request,
            @Parameter(description = "로그인 유저 정보") @AuthenticationPrincipal LoginUser loginUser) {
        return ApiResponse.success("댓글 작성 성공", commentService.createComment(request, loginUser));
    }

    @Operation(
            summary = "댓글 목록 조회",
            description = "댓글 목록 조회 api"
    )
    @PostMapping
    public ApiResponse<CommentListResponse> getComments(
            @Parameter(description = "댓글 목록 조회 Parameter") @ParameterObject CommentListRequest request
    ) {
        return ApiResponse.success("댓글 목록 조회 성공", commentService.getComments(request));
    }
}
