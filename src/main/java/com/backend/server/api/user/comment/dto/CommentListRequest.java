package com.backend.server.api.user.comment.dto;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import com.backend.server.model.entity.enums.TargetType;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public class CommentListRequest extends AbstractPaginationParam<CommentSortType> {
    private TargetType type;
    private Long targetId;

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
        this.direction = direction == null ? Sort.Direction.ASC : direction;
    }
}
