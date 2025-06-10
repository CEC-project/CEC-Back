package com.backend.server.api.admin.community.dto;

import com.backend.server.api.common.dto.AdminAuthorResponse;
import com.backend.server.api.common.dto.BoardResponse;
import com.backend.server.model.entity.Community;
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

    public AdminCommunityResponse(Community community) {
        this.id = community.getId();
        this.title = community.getTitle();
        this.author = community.getAuthor() == null ? null : AdminAuthorResponse.from(community.getAuthor());
        this.recommend = community.getRecommend();
        this.view = community.getView();
        this.board = BoardResponse.from(community.getBoardCategory());
        this.content = community.getContent();
        this.createdAt = community.getCreatedAt();
        this.updatedAt = community.getUpdatedAt();
    }
}
