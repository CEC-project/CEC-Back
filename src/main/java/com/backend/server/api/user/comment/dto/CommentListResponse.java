package com.backend.server.api.user.comment.dto;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentListResponse {
    private List<CommentResponse> comments;
    private PageableInfo pageable;

    public static CommentListResponse from(Page<Comment> parentComments, List<Comment> childComments) {
        List<CommentResponse> responses = CommentResponse.from(
                parentComments.getContent(),
                childComments
        );

        PageableInfo pageable = new PageableInfo(
                parentComments.getNumber(),
                parentComments.getSize(),
                parentComments.getTotalPages(),
                parentComments.getTotalElements()
        );

        return new CommentListResponse(responses, pageable);
    }
}