package com.backend.server.api.user.board.dto;

import com.backend.server.api.common.dto.pagination.SortTypeConvertible;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardSortType implements SortTypeConvertible {
    ID("id");

    private final String field;
}
