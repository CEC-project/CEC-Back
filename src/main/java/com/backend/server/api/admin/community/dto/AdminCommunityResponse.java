package com.backend.server.api.admin.community.dto;

import com.backend.server.api.common.dto.AdminAuthorResponse;
import com.backend.server.api.common.dto.BoardResponse;
import com.backend.server.model.entity.Board;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AdminCommunityResponse {
    private Long id;
    private String title;
    private AdminAuthorResponse author;
    private BoardResponse board;
    private int recommend;
    private int view;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AdminCommunityResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.author = board.getAuthor() == null ? null : AdminAuthorResponse.from(board.getAuthor());
        this.recommend = board.getRecommend();
        this.view = board.getView();
        this.board = BoardResponse.from(board.getBoardCategory());
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
    }
}
