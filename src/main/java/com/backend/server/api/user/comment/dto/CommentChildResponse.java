package com.backend.server.api.user.comment.dto;

import com.backend.server.api.common.dto.AuthorResponse;
import com.backend.server.model.entity.Comment;
import com.backend.server.model.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentChildResponse {
    @Schema(description = "댓글 ID", example = "1")
    private Long id;
    @Schema(description = "댓글 내용", example = "좋은 글 감사합니다.")
    private String content;
    @Schema(description = "부모 댓글 ID", example = "1")
    private Long parentCommentId;
    @Schema(description = "댓글 작성자 정보", implementation = AuthorResponse.class)
    private AuthorResponse authorResponse;
    @Schema(description = "댓글 작성시간", implementation = LocalDateTime.class)
    private LocalDateTime createdAt;
    @Schema(description = "댓글 수정시간", implementation = LocalDateTime.class)
    private LocalDateTime updatedAt;

    public static CommentChildResponse from(Comment comment) {
        User user = comment.getAuthor();
        AuthorResponse authorResponse = new AuthorResponse(
            user.getId(),
            user.getName(),
            user.getNickname(),
            user.getProfilePicture(),
            user.getRole().name()
        );

        return new CommentChildResponse(
            comment.getId(),
            comment.getContent(),
            comment.getParentComment().getId(),
            authorResponse,
            comment.getCreatedAt(),
            comment.getUpdatedAt()
        );
    }
}
