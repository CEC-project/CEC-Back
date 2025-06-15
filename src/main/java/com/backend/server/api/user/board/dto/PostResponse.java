package com.backend.server.api.user.board.dto;

import com.backend.server.api.common.dto.AuthorResponse;
import com.backend.server.api.common.dto.BoardResponse;
import com.backend.server.model.entity.Board;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class PostResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final int recommend;
    private final int view;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final BoardResponse board;
    private final List<String> attachments;
    private final AuthorResponse author;

    public PostResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.recommend = board.getRecommend();
        this.view = board.getView();
        this.board = BoardResponse.from(board.getBoardCategory());
        List<String> attachments = new ArrayList<>();
        if (!StringUtils.isEmpty(board.getAttachmentUrl()))
            attachments = Arrays.stream(board.getAttachmentUrl().split(";")).toList();
        this.attachments = attachments;
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();

        author = AuthorResponse.from(board.getAuthor());
    }
}
