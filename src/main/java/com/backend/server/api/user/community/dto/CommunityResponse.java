package com.backend.server.api.user.community.dto;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.model.entity.Community;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class CommunityResponse {
    private final Long id;
    private final String title;
    private final int recommend;
    private final int view;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String categoryName;
    private final List<String> attachments;
    private final Long authorId;
    private final String authorName;
    private final String authorNickname;
    private final String authorProfilePicture;

    public CommunityResponse(Community community, LoginUser loginuser) {
        this.id = community.getId();
        this.title = community.getTitle();
        this.recommend = community.getRecommend();
        this.view = community.getView();
        this.categoryName = community.getBoardCategory().getName();
        List<String> attachments = new ArrayList<>();
        if (!StringUtils.isEmpty(community.getAttachmentUrl()))
            attachments = Arrays.stream(community.getAttachmentUrl().split(";")).toList();
        this.attachments = attachments;
        this.createdAt = community.getCreatedAt();
        this.updatedAt = community.getUpdatedAt();

        this.authorId = community.getAuthor().getId();
        this.authorName = community.getAuthor().getName();
        this.authorNickname = community.getAuthor().getNickname();
        this.authorProfilePicture = community.getAuthor().getProfilePicture();        
    }
}
