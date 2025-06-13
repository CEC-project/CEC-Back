package com.backend.server.api.user.equipment.dto.equipment;


import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.equipment.Equipment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter 
@AllArgsConstructor
public class EquipmentListResponse {
    private List<EquipmentResponse> content;
    private PageableInfo pageable;

    public EquipmentListResponse(List<EquipmentResponse> content, Page<?> page) {
        this.content = content;
        this.pageable = new PageableInfo(page);
    }
}