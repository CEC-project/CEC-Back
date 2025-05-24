package com.backend.server.api.user.comment.dto;

import com.backend.server.model.entity.Comment;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.TargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    @Schema(description = "댓글 내용", example = "좋은 글 감사합니다.")
    @NotBlank(message = "댓글 내용은 필수 입력 항목입니다.")
    private String content;

    @Schema(description = "댓글 대상 종류", example = "NOTICE")
    @NotNull(message = "댓글 대상 종류는 필수 입력 항목입니다.")
    private TargetType type;

    @Schema(description = "댓글 대상 ID", example = "1")
    @NotNull(message = "댓글 대상 ID는 필수 입력 항목입니다.")
    private Long targetId;

    @Schema(description = "부모 댓글 ID", example = "1")
    private Long parentCommentId;

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