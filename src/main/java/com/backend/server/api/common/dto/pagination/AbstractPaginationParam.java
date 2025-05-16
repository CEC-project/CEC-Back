package com.backend.server.api.common.dto.pagination;

import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;

/**
 * {@code AbstractPaginationParam}은 페이지네이션을 위한 추상 클래스입니다.
 * 하위 클래스에서 구체적인 페이지네이션 파라미터를 정의할 수 있습니다.
 */
@Getter
public abstract class AbstractPaginationParam {
  protected Integer page = 0;
  protected Integer size = 10;
  private PaginationSortType field = PaginationSortType.EMPTY;
  protected Sort.Direction direction = Sort.Direction.DESC;

  public Pageable toPageable() {
    return PageRequest.of(page, size, direction, field.getField());
  }
}
