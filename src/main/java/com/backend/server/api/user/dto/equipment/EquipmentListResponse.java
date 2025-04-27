package com.backend.server.api.user.dto.equipment;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.Equipment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class EquipmentListResponse {
    private List<EquipmentResponse> content;
    private PageableInfo pageable;
    public EquipmentListResponse(Page<Equipment> page) {
        this.content = page.getContent().stream().map(EquipmentResponse::new).toList();
        this.pageable = new PageableInfo(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
    }

}
