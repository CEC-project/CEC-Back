package com.backend.server.api.user.comment.dto;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import com.backend.server.model.entity.enums.TargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
public class CommentListRequest extends AbstractPaginationParam {
    //BOARD, NOTICE, INQUIRY
    @Schema(description = "BOARD, NOTICE, INQUIRY 중 1", implementation = TargetType.class)
    private TargetType type;
    //대상 댓글 아이디
    @Schema(description = "대댓글 대상", implementation = Long.class)
    private Long targetId;

    @Schema(description = "정렬 기준", implementation = CommentSortType.class)
    private CommentSortType sortBy;

    public CommentListRequest(
            TargetType type,
            Long targetId,

            Integer page,
            Integer size,
            CommentSortType sortBy,
            Sort.Direction direction
    ) {
        this.type = type;
        this.targetId = targetId;

        this.page = Math.max(page, 0);
        this.size = Math.max(size, 10);
        this.sortBy = sortBy == null ? CommentSortType.ID : sortBy;
        this.sortDirection = direction == null ? Sort.Direction.ASC : direction;
    }

    public Pageable toPageable() {
        return toPageable(sortBy);
    }
}
