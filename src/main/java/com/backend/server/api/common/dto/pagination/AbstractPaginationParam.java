package com.backend.server.api.common.dto.pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * {@code AbstractPaginationParam}은 페이지네이션을 위한 추상 클래스입니다.
 * 하위 클래스에서 구체적인 페이지네이션 파라미터를 정의할 수 있습니다.
 */
@Getter
@Setter
public abstract class AbstractPaginationParam {

    @Schema(description = "페이지 번호 (기본값 0)", type = "Integer", example = "0")
    protected Integer page;

    @Schema(description = "페이지당 크기 (기본값 = 10)", type = "Integer", example = "10")
    protected Integer size;

    @Schema(description = "정렬 방법", implementation = Sort.Direction.class)
    protected Sort.Direction direction = Sort.Direction.ASC;

    public AbstractPaginationParam() {
        page = 0;
        size = 10;
    }

    public <T extends Enum<T> & SortTypeConvertible> Pageable toPageable(T sortBy) {
        return PageRequest.of(page, size, direction, sortBy.getField());
    }
}
