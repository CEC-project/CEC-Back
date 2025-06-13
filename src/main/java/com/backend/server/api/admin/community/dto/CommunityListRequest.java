package com.backend.server.api.admin.community.dto;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import com.backend.server.api.user.board.dto.BoardSortType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Data
public class CommunityListRequest extends AbstractPaginationParam {
    public enum CommunitySearchType {
        ALL, TITLE, CONTENT, NAME, NICKNAME
    }


    @Schema(description = "공지사항 검색 기준", implementation = CommunitySearchType.class)
    CommunitySearchType searchType ;

    @Schema(description = "검색어")
    String searchKeyword;

    @Schema(description = "카테고리 ID로 정렬")
    private Long categoryId;



    BoardSortType sortBy;

    public CommunityListRequest(
            CommunitySearchType searchType,
            String searchKeyword,

            Integer page,
            Integer size,
            BoardSortType sortBy,
            Sort.Direction direction
    ) {
        this.searchType = searchType == null ? CommunitySearchType.ALL : searchType;
        this.searchKeyword = searchKeyword;

        this.page = Math.max(page, 0);
        this.size = Math.max(size, 10);
        this.sortBy = sortBy == null ? BoardSortType.ID : sortBy;
        this.sortDirection = direction == null ? Sort.Direction.ASC : direction;
    }

    public Pageable toPageable() {
        return toPageable(sortBy);
    }
}
