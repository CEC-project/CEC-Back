package com.backend.server.api.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentListRequest {
    private Integer page;            // 페이지 번호 (기본값: 1)
    private Integer size;            // 페이지 크기 (기본값: 17)
    private String category;         // 장비 분류
    private String status;           // 장비 상태 (NORMAL|DAMAGED|RETURN IN PROGRESS)
    private Boolean available;       // 대여 가능 여부
    private String searchKeyword;    // 검색어 (장비 이름, 모델명)
    private Integer searchType;      // 검색 유형 (0:name|1:model_name| 나머ㅏ지는 뭐하지...)
    private String sortBy;           // 정렬 기준 (name|category|status)
    private String sortDirection;    // 정렬 방향 (asc|desc)
    private Boolean favoriteOnly;    // 즐겨찾기한 장비만 필터링
}
