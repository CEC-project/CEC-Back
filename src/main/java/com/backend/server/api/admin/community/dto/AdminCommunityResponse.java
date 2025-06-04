package com.backend.server.api.admin.community.dto;

import com.backend.server.model.entity.Community;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AdminCommunityResponse {
    private Long id;
    private String title;
    private String nickname;
    private Long authorId;
    private String authorName;
    private String authorNickname;
    private int recommend;
    private int view;
    private Long boardCategoryId;
    private String boardCategoryName;
    private String content;


    public AdminCommunityResponse(Community community) {
        this.id = community.getId();
        this.title = community.getTitle();
        this.nickname = community.getNickname();
        this.authorId = community.getAuthor().getId();
        this.authorName = community.getAuthor().getName();
        this.authorNickname = community.getAuthor().getNickname();
        this.recommend = community.getRecommend();
        this.view = community.getView();
        this.boardCategoryId = community.getBoardCategory().getId();
        this.boardCategoryName = community.getBoardCategory().getName();
        this.content = community.getContent();
    }
}
