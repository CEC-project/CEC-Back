package com.backend.server.api.admin.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserListRequest {
    private int page = 0;
    private int size = 10;
    private String searchKeyword;
    private Integer searchType; // 검색 유형 (0:name|1:phoneNumber|2:studentNumber)
    private Integer grade; // 1 or 2 or 3 or 4 or null
    private String gender; // '남' or '여' or null
    private Long professorId; // 교수 테이블의 id or null
    private Integer sortBy = 0; // 정렬 기준 (0:name|1:studentNumber|2:restrictionCount)
    private String sortDirection = "asc"; // asc, desc
}