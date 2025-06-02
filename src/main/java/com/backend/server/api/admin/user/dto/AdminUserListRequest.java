package com.backend.server.api.admin.user.dto;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserListRequest extends AbstractPaginationParam<AdminUserSortType> {

    public enum AdminUserSearchType {
        NAME, PHONE_NUMBER, STUDENT_NUMBER, NICKNAME, ALL
    }

    public enum Gender {
        M, F
    }

    @Schema(description = "검색 키워드", example = "홍길동", nullable = true)
    private String searchKeyword;

    @Schema(description = "검색 유형 NAME, PHONE_NUMBER, STUDENT_NUMBER, NICKNAME, ALL", example = "ALL", nullable = true)
    private AdminUserSearchType searchType;

    @Schema(description = "학년 (1, 2, 3, 4 중 하나)", example = "2", nullable = true, implementation = Integer.class)
    private Integer grade;

    @Schema(description = "성별 M/F", nullable = true)
    private Gender gender;

    @Schema(description = "교수 ID (professor 테이블의 PK)", example = "102", nullable = true)
    private Long professorId;

    public AdminUserSearchType getSearchType() {
        return searchType == null ? AdminUserSearchType.ALL : searchType;
    }

    @Override
    @Schema(implementation = AdminUserSortType.class)
    public AdminUserSortType getSortBy() {
        return sortBy == null ? AdminUserSortType.getDefault() : sortBy;
    }

    @Override
    public Pageable toPageable() {
        return PageRequest.of(page, size, direction, getSortBy().getField());
    }
}