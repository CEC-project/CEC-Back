package com.backend.server.api.user.dto.equipment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.Equipment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class FavoriteListResponse {
    private List<EquipmentResponse> content;
    private PageableInfo pageable;
    
    
    public FavoriteListResponse(Page<Equipment> page) {
        this.content = page.getContent().stream()
                .map(EquipmentResponse::new)
                .collect(Collectors.toList());
        this.pageable = new PageableInfo(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}
