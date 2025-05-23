package com.backend.server.api.user.comment.dto;

import com.backend.server.api.common.dto.ProfileResponse;
import com.backend.server.model.entity.Comment;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.TargetType;

public record CommentResponse(
        Long id,
        TargetType type,
        Long targetId,
        String content,
        Long parentCommentId,
        ProfileResponse author
) {
    public static CommentResponse from(Comment comment) {
        User user = comment.getAuthor();

        return new CommentResponse(
                comment.getId(),
                comment.getType(),
                comment.getTargetId(),
                comment.getContent(),
                comment.getParentComment() == null ? null : comment.getParentComment().getId(),
                new ProfileResponse(
                        user.getId(),
                        user.getName(),
                        user.getNickname(),
                        user.getProfilePicture(),
                        user.getRole().name()
                )
        );
    }
}