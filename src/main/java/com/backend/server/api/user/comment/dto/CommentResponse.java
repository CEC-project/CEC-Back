package com.backend.server.api.user.comment.dto;

import com.backend.server.api.common.dto.AuthorResponse;
import com.backend.server.model.entity.Comment;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.TargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CommentResponse {
    @Schema(description = "댓글 ID", example = "1")
    private Long id;
    @Schema(description = "댓글 종류", implementation = TargetType.class)
    private TargetType type;
    @Schema(description = "댓글 대상 ID", example = "1")
    private Long targetId;
    @Schema(description = "댓글 내용", example = "좋은 글 감사합니다.")
    private String content;
    @Schema(description = "대댓글")
    private List<CommentChildResponse> childComments;
    @Schema(description = "댓글 작성자 정보", implementation = AuthorResponse.class)
    private AuthorResponse authorResponse;
    @Schema(description = "댓글 작성시간", implementation = LocalDateTime.class)
    private LocalDateTime createdAt;
    @Schema(description = "댓글 수정시간", implementation = LocalDateTime.class)
    private LocalDateTime updatedAt;

    private CommentResponse(Comment comment, List<Comment> childComments) {
        User user = comment.getAuthor();

        this.id = comment.getId();
        this.type = comment.getType();
        this.targetId = comment.getTargetId();
        this.content = comment.getContent();
        this.childComments = childComments == null || childComments.isEmpty()
            ? List.of()
            : childComments.stream()
            .map(CommentChildResponse::from)
            .toList();

        this.authorResponse = new AuthorResponse(
            user.getId(),
            user.getName(),
            user.getNickname(),
            user.getProfilePicture(),
            user.getRole().name()
        );
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }

    public static List<CommentResponse> from(List<Comment> parentComments, List<Comment> childComments) {
        if (parentComments.isEmpty()) {
            return List.of();
        }

        Map<Long, List<Comment>> childCommentsMap = childComments.stream()
            .collect(Collectors.groupingBy(comment -> comment.getParentComment().getId()));

        return parentComments.stream()
            .map(parent -> {
                List<Comment> children = childCommentsMap.getOrDefault(parent.getId(), List.of());
                return new CommentResponse(parent, children);
            })
            .toList();
    }
}