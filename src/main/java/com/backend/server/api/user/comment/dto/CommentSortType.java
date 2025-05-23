package com.backend.server.api.user.comment.dto;

import com.backend.server.api.common.dto.pagination.SortTypeConvertible;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommentSortType implements SortTypeConvertible {
    ID("id");

    private final String field;
}
