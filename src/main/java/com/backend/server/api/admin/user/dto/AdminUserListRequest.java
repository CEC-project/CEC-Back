package com.backend.server.api.admin.user.dto;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
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
    private String searchKeyword;
    private Integer searchType; // 검색 유형 (0:name|1:phoneNumber|2:studentNumber|3:nickname|4 or 생략:all)
    private Integer grade; // 1 or 2 or 3 or 4 or null
    private String gender; // '남' or '여' or null
    private Long professorId; // 교수 테이블의 id or null
}