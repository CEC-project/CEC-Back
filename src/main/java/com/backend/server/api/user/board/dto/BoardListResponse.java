package com.backend.server.api.user.board.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.Board;

import lombok.Getter;

@Getter
public class BoardListResponse {
    private List<BoardResponse> content;
    private PageableInfo pageable;

    public BoardListResponse(Page<Board> page, LoginUser loginuser) {
        this.content = page.getContent().stream()
            .map(community -> new BoardResponse(community, loginuser))
            .toList();
        this.pageable = new PageableInfo(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
    }
}
