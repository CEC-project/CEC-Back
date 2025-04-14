package com.backend.server.api.admin.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserListRequest {
    private int page = 0;
    private int size = 10;
    private String searchKeyword;
    private Integer searchType; // 검색 유형 (0:name|1:phoneNumber|2:studentNumber)
    private String year;
    private String gender;
    private String professor;
    private Integer sortBy = 0; // 정렬 기준 (0:name|1:studentNumber|2:restrictionCount)
    private String sortDirection = "asc"; // asc, desc
}