package com.backend.server.api.user.community.dto;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.model.entity.Community;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommunityResponse {
    private Long id;
    private String title;
    private Long community_type_id;
    private int recommand;
    private int view;
    private String type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long authorId;
    private String authorName;
    private String authorNickname;
    private String authorProfilePicture;

    public CommunityResponse(Community community, LoginUser loginuser) {
        this.id = community.getId();
        this.title = community.getTitle();
        this.authorId = community.getAuthor().getId();
        this.recommand = community.getRecommand();
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
