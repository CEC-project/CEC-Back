package com.backend.server.api.user.comment.dto;

import com.backend.server.model.entity.Comment;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.TargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(
        @Schema(description = "댓글 내용", example = "좋은 글 감사합니다.")
        @NotBlank(message = "공지사항 이름은 필수 입력 항목입니다.")
        String content,

        @Schema(description = "댓글 대상 종류", example = "NOTICE")
        @NotNull(message = "댓글 재상 종류는 필수 입력 항목입니다.")
        TargetType type,

        @Schema(description = "댓글 대상 ID", example = "1")
        @NotNull(message = "댓글 대상 ID는 필수 입력 항목입니다.")
        Long targetId,

        @Schema(description = "부모 댓글 ID", example = "1")
        Long parentCommentId
) {
    public Comment toEntity(Comment parentComment, User author) {
        return Comment.builder()
                .content(content)
                .type(type)
                .targetId(targetId)
                .author(author)
                .parentComment(parentComment)
                .build();
    }
}
