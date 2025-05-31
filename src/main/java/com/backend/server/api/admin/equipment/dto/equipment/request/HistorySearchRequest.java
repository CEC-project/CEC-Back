package com.backend.server.api.admin.equipment.dto.equipment.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class HistorySearchRequest {
    private String targetType;   // "EQUIPMENT", "CLASSROOM", or null
    private String historyType;  // "BROKEN", "REPAIR", or null
    private String nameKeyword;  // 모델명 or 강의실명 or 일련번호
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private int page = 0;
    private int size = 10;
}