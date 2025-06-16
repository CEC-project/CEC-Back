package com.backend.server.api.user.board.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.Board;

import lombok.Getter;

@Getter
public class PostListResponse {
    private List<PostResponse> content;
    private PageableInfo pageable;

    public PostListResponse(Page<Board> page) {
        this.content = page.getContent().stream()
            .map(PostResponse::new)
            .toList();
        this.pageable = new PageableInfo(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
    }
}
