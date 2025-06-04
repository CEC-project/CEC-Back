package com.backend.server.api.admin.community.dto;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCommunityListRequest extends AbstractPaginationParam {

    public enum AdminCommunitySearchType {
        NAME, TITLE, CONTENT, NICKNAME, ALL
    }
    
    @Schema(description = "검색 키워드", example = "홍길동", nullable = true)
    private String searchKeyword;

    @Schema(description = "검색 유형 : 생략시 ALL", implementation = AdminCommunitySearchType.class)
    private AdminCommunitySearchType searchType;

    public AdminCommunitySearchType getSearchType() {
        return searchType == null ? AdminCommunitySearchType.ALL : searchType;
    }

    @Schema(description = "정렬 기준 (기본값 ID)", implementation = AdminCommunitySortType.class)
    private AdminCommunitySortType sortBy;

    public AdminCommunitySortType getSortBy() {
        return sortBy == null ? AdminCommunitySortType.ID : sortBy;
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size, sortDirection, getSortBy().getField());
    }
}
