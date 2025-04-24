package com.backend.server.api.admin.dto;

import com.backend.server.model.entity.Equipment;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class AdminEquipmentListResponse {
    private final List<Equipment> content;
    private final int totalPages;
    private final long totalElements;
    private final int currentPage;
    private final int size;

    public AdminEquipmentListResponse(Page<Equipment> equipmentsPage) {
        this.content = equipmentsPage.getContent();
        this.totalPages = equipmentsPage.getTotalPages();
        this.totalElements = equipmentsPage.getTotalElements();
        this.currentPage = equipmentsPage.getNumber();
        this.size = equipmentsPage.getSize();
    }
}
