package com.backend.server.api.admin.history.dto;

import com.backend.server.api.common.dto.PageableInfo;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class AdminBrokenRepairHistoryListResponse {
    private List<AdminBrokenRepairHistoryResponse> content;
    private PageableInfo pageable;


    public AdminBrokenRepairHistoryListResponse(List<AdminBrokenRepairHistoryResponse> content, Page<?> page) {
        this.content = content;
        this.pageable = new PageableInfo(page);
    }
}
