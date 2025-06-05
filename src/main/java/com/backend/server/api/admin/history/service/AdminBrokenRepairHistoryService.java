package com.backend.server.api.admin.history.service;

import com.backend.server.api.admin.equipment.dto.equipment.response.AdminEquipmentListResponse;
import com.backend.server.api.admin.equipment.dto.equipment.response.AdminEquipmentResponse;
import com.backend.server.api.admin.history.dto.AdminBrokenRepairHistoryListResponse;
import com.backend.server.api.admin.history.dto.AdminBrokenRepairHistoryRequest;
import com.backend.server.api.admin.history.dto.AdminBrokenRepairHistoryResponse;
import com.backend.server.model.entity.BrokenRepairHistory;
import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.repository.BrokenRepairHistoryRepository;
import com.backend.server.model.repository.BrokenRepairHistorySpecification;
import com.backend.server.model.repository.equipment.EquipmentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminBrokenRepairHistoryService {
    private final BrokenRepairHistoryRepository brokenRepairHistoryRepository;
    public AdminBrokenRepairHistoryListResponse getBrokenRepairHistory(AdminBrokenRepairHistoryRequest request) {
        Pageable pageable = request.toPageable();

        Specification<BrokenRepairHistory> spec = BrokenRepairHistorySpecification.filter(request);
        Page<BrokenRepairHistory> page = brokenRepairHistoryRepository.findAll(spec, pageable);

        List<AdminBrokenRepairHistoryResponse> responses = page.getContent().stream()
                .map(AdminBrokenRepairHistoryResponse::new)
                .toList();
        return new AdminBrokenRepairHistoryListResponse(responses, page);
    }
}
