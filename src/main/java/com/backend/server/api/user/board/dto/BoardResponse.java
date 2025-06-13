package com.backend.server.api.user.board.dto;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.model.entity.Board;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class BoardResponse {
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

    public BoardResponse(Board board, LoginUser loginuser) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.recommend = board.getRecommend();
        this.view = board.getView();
        this.categoryName = board.getBoardCategory().getName();
        List<String> attachments = new ArrayList<>();
        if (!StringUtils.isEmpty(board.getAttachmentUrl()))
            attachments = Arrays.stream(board.getAttachmentUrl().split(";")).toList();
        this.attachments = attachments;
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();

        this.authorId = board.getAuthor().getId();
        this.authorName = board.getAuthor().getName();
        this.authorNickname = board.getAuthor().getNickname();
        this.authorProfilePicture = board.getAuthor().getProfilePicture();
    }
}
