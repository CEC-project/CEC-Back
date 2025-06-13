package com.backend.server.api.admin.community.dto;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdminCommunityListResponse {
    private List<AdminCommunityResponse> content;
    private PageableInfo pageable;

    public AdminCommunityListResponse(Page<Board> page) {
        this.content = page.getContent().stream()
                .map(AdminCommunityResponse::new)
                .toList();
        this.pageable = new PageableInfo(page);
    }
}
