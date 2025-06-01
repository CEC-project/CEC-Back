package com.backend.server.api.user.community.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.Community;

import lombok.Getter;

@Getter
public class CommunityListResponse {
    private List<CommunityResponse> content;
    private PageableInfo pageable;

    public CommunityListResponse(Page<Community> page, LoginUser loginuser) {
        this.content = page.getContent().stream()
            .map(community -> new CommunityResponse(community, loginuser))
            .toList();
        this.pageable = new PageableInfo(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
    }
}
