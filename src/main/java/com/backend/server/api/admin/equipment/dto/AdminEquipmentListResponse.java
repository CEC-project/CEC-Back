package com.backend.server.api.admin.equipment.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.backend.server.api.common.dto.PageInfo;
import com.backend.server.model.entity.Equipment;
import lombok.Getter;

@Getter
public class AdminEquipmentListResponse {
    private final List<AdminEquipmentResponse> content;
    private final PageInfo pageable;
    
    public AdminEquipmentListResponse(List<AdminEquipmentResponse> content, Page<?> page) {
        this.content = content;
        this.pageable = new PageInfo(page);
    }
    
    // 추가된 생성자: 페이지네이션 없이 단순 목록과 총 개수만 반환
    public AdminEquipmentListResponse(List<AdminEquipmentResponse> content, int totalSize) {
        this.content = content;
        this.pageable = new PageInfo(totalSize, content.size(), 1, 0);
    }
}
