package com.backend.server.api.user.comment.dto;

import com.backend.server.api.common.dto.ProfileResponse;
import com.backend.server.model.entity.Comment;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.TargetType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private TargetType type;
    private Long targetId;
    private String content;
    private Long parentCommentId;
    private List<CommentResponse> childComments;
    private ProfileResponse author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private CommentResponse(Comment comment, List<Comment> childComments) {
        User user = comment.getAuthor();

        this.id = comment.getId();
        this.type = comment.getType();
        this.targetId = comment.getTargetId();
        this.content = comment.getContent();
        this.parentCommentId = comment.getParentComment() == null
                ? null
                : comment.getParentComment().getId();
        this.childComments = childComments == null || childComments.isEmpty()
                ? List.of()
                : childComments.stream()
                .map(child -> new CommentResponse(child, null))
                .toList();

        this.author = new ProfileResponse(
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