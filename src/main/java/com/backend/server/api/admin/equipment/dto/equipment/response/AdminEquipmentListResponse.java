package com.backend.server.api.admin.equipment.dto.equipment.response;

import com.backend.server.api.common.dto.PageableInfo;
import java.util.List;
import org.springframework.data.domain.Page;
import lombok.Getter;

@Getter
public class AdminEquipmentListResponse {
    private final List<AdminEquipmentResponse> content;
    private final PageableInfo pageable;
    
    public AdminEquipmentListResponse(List<AdminEquipmentResponse> content, Page<?> page) {
        this.content = content;
        this.pageable = new PageableInfo(page);
    }
    
    // 추가된 생성자: 페이지네이션 없이 단순 목록과 총 개수만 반환
    public AdminEquipmentListResponse(List<AdminEquipmentResponse> content, int totalSize) {
        this.content = content;
        this.pageable = new PageableInfo(totalSize, content.size(), 1, 0);
    }
}
