package com.backend.server.api.user.board.dto;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class PostListRequest extends AbstractPaginationParam {
    public enum CommunitySearchType {
        ALL, TITLE, CONTENT, NAME, NICKNAME
    }

    @Schema(description = "게시글 검색 기준", implementation = CommunitySearchType.class)
    CommunitySearchType searchType ;

    @Schema(description = "검색어")
    String searchKeyword;

    @Schema(description = "카테고리 ID로 정렬")
    private Long categoryId;

    @Schema(description = "작성자 id")
    private Long authorId;

    PostSortType sortBy;

    public Pageable toPageable() {
        if (sortBy == null)
            sortBy = PostSortType.ID;
        return toPageable(sortBy);
    }
}
