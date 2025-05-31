package com.backend.server.api.admin.history.service;

import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentBrokenOrRepairRequest;
import com.backend.server.api.admin.history.dto.RepairBrokenHistoryResponse;
import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.repository.equipment.EquipmentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BrokenOrRepairHistoryService {
//    public RepairBrokenHistoryResponse getAdminEquipmentBrokenOrRepairResponse(AdminEquipmentBrokenOrRepairRequest request){
//        Pageable pageable = EquipmentSpecification.getPageable(request);
//
//        Specification<Equipment> spec = EquipmentSpecification.adminFilterEquipments(request);
//        Page<Equipment> page = equipmentRepository.findAll(spec, pageable);
//
//        List<RepairBrokenHistoryResponse> responses = page.getContent().stream()
//                .map(RepairBrokenHistoryResponse::new)
//                .toList();
//        return new RepairBrokenHistoryResponse(responses, page);
//    }
}
