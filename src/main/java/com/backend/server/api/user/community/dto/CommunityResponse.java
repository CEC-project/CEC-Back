package com.backend.server.api.user.community.dto;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.model.entity.Community;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommunityResponse {
    private final Long id;
    private final String title;
    private final Long community_type_id;
    private final int recommend;
    private final int view;
    private final String type;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private final Long authorId;
    private final String authorName;
    private final String authorNickname;
    private final String authorProfilePicture;

    public CommunityResponse(Community community, LoginUser loginuser) {
        this.id = community.getId();
        this.title = community.getTitle();
        this.recommend = community.getRecommend();
        this.view = community.getView();
        this.type = community.getType();
        this.community_type_id = community.getTypeId();
        this.createdAt = community.getCreatedAt();
        this.updatedAt = community.getUpdatedAt();

        this.authorId = community.getAuthor().getId();
        this.authorName = community.getAuthor().getName();
        this.authorNickname = community.getAuthor().getNickname();
        this.authorProfilePicture = community.getAuthor().getProfilePicture();        
    }
}
