package com.backend.server.api.user.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.backend.server.model.entity.Equipment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteListResponse {
    private List<EquipmentResponse> favorites;
    private int count;
    
    public FavoriteListResponse(List<Equipment> equipments) {
        this.favorites = equipments.stream()
                .map(EquipmentResponse::new)
                .collect(Collectors.toList());
        this.count = equipments.size();
    }
}
