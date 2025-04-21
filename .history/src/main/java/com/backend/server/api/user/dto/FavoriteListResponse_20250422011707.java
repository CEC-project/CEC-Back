package com.backend.server.api.user.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
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
    private long totalElements;
    private int totalPages;
    private int number;
    private int size;
    private boolean first;
    private boolean last;
    
    public FavoriteListResponse(List<Equipment> equipments) {
        this.favorites = equipments.stream()
                .map(EquipmentResponse::new)
                .collect(Collectors.toList());
        this.count = equipments.size();
    }
    
    public FavoriteListResponse(Page<Equipment> page) {
        this.favorites = page.getContent().stream()
                .map(EquipmentResponse::new)
                .collect(Collectors.toList());
        this.count = page.getNumberOfElements();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.number = page.getNumber();
        this.size = page.getSize();
        this.first = page.isFirst();
        this.last = page.isLast();
    }
}
