package com.backend.server.api.user.history.service;

import com.backend.server.api.admin.history.dto.AdminBrokenRepairHistoryResponse;
import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.api.user.classroom.dto.ClassroomResponse;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentResponse;
import com.backend.server.api.user.history.dto.RentalHistoryListRequest;
import com.backend.server.api.user.history.dto.RentalHistoryListResponse;
import com.backend.server.api.user.history.dto.RentalHistoryResponse;
import com.backend.server.model.entity.RentalHistory;
import com.backend.server.model.repository.history.RentalHistoryRepository;
import com.backend.server.model.repository.history.RentalHistorySpecification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalHistoryService {

    private final RentalHistoryRepository rentalHistoryRepository;

    public RentalHistoryListResponse getRentalHistoryList(RentalHistoryListRequest request) {
        Specification<RentalHistory> spec = RentalHistorySpecification.filter(request);
        Pageable pageable = request.toPageable();
        Page<RentalHistory> page = rentalHistoryRepository.findAll(spec, pageable);

        List<RentalHistoryResponse> content = page.stream()
                .map(rentalHistory -> {
                    ClassroomResponse classroom = null;
                    if (rentalHistory.getClassroom() != null)
                        classroom = new ClassroomResponse(rentalHistory.getClassroom());

                    EquipmentResponse equipment = null;
                    if (rentalHistory.getEquipment() != null)
                        equipment = new EquipmentResponse(rentalHistory.getEquipment());

                    AdminBrokenRepairHistoryResponse broken = null;
                    if (rentalHistory.getBrokenHistory() != null)
                        broken = new AdminBrokenRepairHistoryResponse(rentalHistory.getBrokenHistory());

                    AdminBrokenRepairHistoryResponse repair = null;
                    if (rentalHistory.getRepairHistory() != null)
                        repair =  new AdminBrokenRepairHistoryResponse(rentalHistory.getRepairHistory());

                    return new RentalHistoryResponse(rentalHistory, classroom, equipment, broken, repair);
                })
                .toList();
        return new RentalHistoryListResponse(content, new PageableInfo(page));
    }
}
