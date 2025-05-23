package com.backend.server.api.user.comment.dto;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.Comment;
import org.springframework.data.domain.Page;

import java.util.List;

public record CommentListResponse(
        List<CommentResponse> comments,
        PageableInfo pageable
) {
    public static CommentListResponse fromPage(Page<Comment> page) {
        List<CommentResponse> comments = page.getContent().stream()
                .map(CommentResponse::from)
                .toList();
        PageableInfo pageable = new PageableInfo(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
        return new CommentListResponse(comments, pageable);
    }
}
