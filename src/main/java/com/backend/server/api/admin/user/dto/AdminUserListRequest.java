package com.backend.server.api.admin.user.dto;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserListRequest extends AbstractPaginationParam<AdminUserSortType> {

    @Schema(description = "검색 키워드", example = "홍길동", nullable = true)
    private String searchKeyword;

    @Schema(description = "검색 유형 (0: 이름, 1: 전화번호, 2: 학번, 3: 닉네임, 4 또는 생략: 전체)", example = "0", nullable = true)
    private Integer searchType;

    @Schema(description = "학년 (1, 2, 3, 4 중 하나)", example = "2", nullable = true)
    private Integer grade;

    @Schema(description = "성별 ('남' 또는 '여')", example = "남", nullable = true)
    private String gender;

    @Schema(description = "교수 ID (professor 테이블의 PK)", example = "102", nullable = true)
    private Long professorId;

    public Integer getSearchType() {
        return searchType == null ? 4 : searchType;
    }
}