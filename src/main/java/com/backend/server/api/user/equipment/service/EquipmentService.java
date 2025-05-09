package com.backend.server.api.user.equipment.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.server.api.user.equipment.dto.equipment.EquipmentListResponse;
import com.backend.server.model.repository.EquipmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;

    // public EquipmentListResponse getEquipmentList(Long userId, Pageable pageable) {
    //     List<EquipmentResponse> responses = page.getContent().stream()
    //     .map(equipment -> new EquipmentResponse(
    //         equipment,
    //         modelName,      // 서비스에서 조회
    //         renterName,     // 서비스에서 조회
    //         startRentDate,  // 서비스에서 조회
    //         endRentDate     // 서비스에서 조회
    //     ))
    //     .toList();
    //    return new EquipmentListResponse(responses, page);
    // }
}
