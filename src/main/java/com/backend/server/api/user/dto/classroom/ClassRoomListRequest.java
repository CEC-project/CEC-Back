package com.backend.server.api.user.dto.classroom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomListRequest {
    private Integer page;            // 페이지 번호
    private Integer size;            // 페이지 크기
    private String status;           // 강의실 상태
    private Boolean available;       // 대여 가능 여부
    private String searchKeyword;    // 검색어
    private Integer searchType;      // 검색 유형 (0: 강의실 이름, 1: 위치)
    private String sortBy;           // 정렬 기준
    private String sortDirection;    // 정렬 방향
    private Boolean favoriteOnly;    // 즐겨찾기한 강의실만 필터링
} 