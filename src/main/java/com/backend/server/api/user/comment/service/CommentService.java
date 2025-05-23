package com.backend.server.api.user.comment.service;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.comment.dto.CommentIdResponse;
import com.backend.server.api.user.comment.dto.CommentListRequest;
import com.backend.server.api.user.comment.dto.CommentListResponse;
import com.backend.server.api.user.comment.dto.CommentRequest;
import com.backend.server.model.entity.Comment;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.TargetType;
import com.backend.server.model.repository.CommentRepository;
import com.backend.server.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final Map<TargetType, JpaRepository<?, Long>> targetMap;

    public CommentIdResponse createComment(CommentRequest request, LoginUser loginUser) {
        User currentuser = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (targetMap.get(request.type()).findById(request.targetId()).isEmpty()) {
            throw new RuntimeException("댓글 대상을 찾을 수 없습니다.");
        }

        Comment parentComment = commentRepository.findById(request.parentCommentId()).orElse(null);
        Comment comment = request.toEntity(parentComment, currentuser);
        commentRepository.save(comment);
        return new CommentIdResponse(comment.getId());
    }

    public CommentListResponse getComments(CommentListRequest request) {
        List<Comment> comments = commentRepository.findAllByTargetId(request.targetId());
        return new CommentListResponse(comments);
    }
}
