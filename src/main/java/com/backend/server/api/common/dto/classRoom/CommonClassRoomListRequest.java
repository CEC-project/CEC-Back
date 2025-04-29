package com.backend.server.api.common.dto.classRoom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonClassRoomListRequest {
    private Integer page;            // 페이지 번호 (기본값: 1)
    private Integer size;            // 페이지 크기 (기본값: 17)
    private String status;           // 장비 상태 (NORMAL|DAMAGED|RETURN IN PROGRESS)
    private Boolean available;       // 대여 가능 여부
    private String searchKeyword;    // 검색어 (장비 이름, 모델명)
    private Integer searchType;      // 검색 유형 (강의실 이름, 위치치)
    private String sortBy;           // 정렬 기준 (name, .위치치)
    private String sortDirection;    // 정렬 방향 (asc|desc)
    private Boolean favoriteOnly;    // 즐겨찾기한 강의실실만 필터링
}
