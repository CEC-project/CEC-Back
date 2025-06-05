package com.backend.server.api.admin.history.dto;

import com.backend.server.api.admin.community.dto.AdminCommunityResponse;
import com.backend.server.api.admin.equipment.dto.equipment.response.AdminEquipmentResponse;
import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.BrokenRepairHistory;
import com.backend.server.model.entity.Community;
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
