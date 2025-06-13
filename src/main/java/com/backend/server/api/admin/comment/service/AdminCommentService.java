package com.backend.server.api.admin.comment.service;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.common.notification.service.CommonNotificationService;
import com.backend.server.api.user.comment.dto.CommentListRequest;
import com.backend.server.api.user.comment.dto.CommentListResponse;
import com.backend.server.api.user.comment.dto.CommentRequest;
import com.backend.server.api.user.comment.dto.CommentUpdateRequest;
import com.backend.server.model.entity.*;
import com.backend.server.model.entity.enums.Role;
import com.backend.server.model.entity.enums.TargetType;
import com.backend.server.model.repository.CommentRepository;
import com.backend.server.model.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminCommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommonNotificationService notificationService;
    private final Map<TargetType, JpaRepository<?, Long>> targetMap;

    public Long createComment(CommentRequest request, LoginUser loginUser) {
        User currentuser = userRepository.findById(loginUser.getId())
            .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        if (targetMap.get(request.getType()).findById(request.getTargetId()).isEmpty()) {
            throw new EntityNotFoundException("댓글 대상을 찾을 수 없습니다.");
        }

        Object targetEntity = targetMap.get(request.getType()).findById(request.getTargetId())
                .orElseThrow(() -> new EntityNotFoundException("댓글 대상을 찾을 수 없습니다."));

        Comment parentComment = request.getParentCommentId() == null ? null
            : commentRepository.findById(request.getParentCommentId())
            .orElseThrow(() -> new EntityNotFoundException("원본 댓글을 찾을 수 없습니다."));
        Comment comment = request.toEntity(parentComment, currentuser);
        commentRepository.save(comment);

        Long targetAuthorId = null;
        String link = "#";
        String targetTitle = "";
        switch (request.getType()) {
            case BOARD -> {
                Board board = (Board) targetEntity;
                targetAuthorId = board.getAuthor().getId();
                link = "/community/" + board.getId();
                targetTitle = board.getTitle();
            }
            case NOTICE -> {
                Notice notice = (Notice) targetEntity;
                targetAuthorId = notice.getAuthor().getId();
                link = "/notice/" + notice.getId();
                targetTitle = notice.getTitle();
            }
            case INQUIRY -> {
                Inquiry inquiry = (Inquiry) targetEntity;
                targetAuthorId = inquiry.getAuthor().getId();
                link = "/inquiry/" + inquiry.getId();
                targetTitle = inquiry.getTitle();
            }
            default -> {}
        }
        // 1) 원글 작성자에게 알림 (본인이면 제외)
        if (targetAuthorId != null && !targetAuthorId.equals(loginUser.getId())) {
            notificationService.notificationProcess(
                    targetAuthorId,
                    "댓글",
                    targetTitle + "에 댓글이 달렸습니다.",
                    loginUser.getNickname() + "님이 " + targetTitle + "에 댓글을 남겼습니다.",
                    link
            );
        }

        // 2) 대댓글이라면, 부모 댓글 작성자에게 알림 (본인/원글작성자와 중복시 제외)
        if (parentComment != null) {
            Long parentAuthorId = parentComment.getAuthor().getId();
            if (!parentAuthorId.equals(loginUser.getId()) &&
                    (targetAuthorId == null || !parentAuthorId.equals(targetAuthorId))) {
                notificationService.notificationProcess(
                        parentAuthorId,
                        "대댓글",
                        "내 댓글에 대댓글이 달렸습니다.",
                        loginUser.getNickname() + "님이 내 댓글에 답글을 남겼습니다.",
                        link + "#comment-" + parentComment.getId()
                );
            }
        }

        return comment.getId();
    }

    public CommentListResponse getComments(CommentListRequest request) {
        Page<Comment> parentComments = commentRepository.findAllByTypeAndTargetIdAndParentCommentIsNullWithAuthor(
            request.getType(), request.getTargetId(), request.toPageable()
        );

        List<Long> parentIds = parentComments.getContent().stream()
            .map(Comment::getId)
            .toList();

        List<Comment> childComments = parentIds.isEmpty() ? List.of() :
            commentRepository.findAllByParentCommentIdInWithAuthor(parentIds);

        return CommentListResponse.from(parentComments, childComments);
    }

    public Long updateComment(CommentUpdateRequest request, Long commentId, LoginUser loginUser) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        if (comment.getAuthor().getRole().equals(Role.ROLE_SUPER_ADMIN) && loginUser.getRole().equals(Role.ROLE_ADMIN)) {
            throw new AccessDeniedException("권한이 부족합니다.");
        }

        comment = comment.toBuilder()
            .content(request.getContent())
            .build();

        Comment updated = commentRepository.save(comment);
        return updated.getId();
    }

    public Long deleteComment(Long commentId, LoginUser loginUser) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        if (comment.getAuthor().getRole().equals(Role.ROLE_SUPER_ADMIN) && loginUser.getRole().equals(Role.ROLE_ADMIN)) {
            throw new AccessDeniedException("권한이 부족합니다.");
        }

        commentRepository.delete(comment);
        return comment.getId();
    }
}
