package com.backend.server.api.user.history.service;

import com.backend.server.api.admin.history.dto.AdminBrokenRepairHistoryResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.api.user.classroom.dto.ClassroomResponse;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentResponse;
import com.backend.server.api.user.history.dto.RentalHistoryListRequest;
import com.backend.server.api.user.history.dto.RentalHistoryListResponse;
import com.backend.server.api.user.history.dto.RentalHistoryResponse;
import com.backend.server.model.entity.RentalHistory;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.history.RentalHistoryRepository;
import com.backend.server.model.repository.history.RentalHistorySpecification;
import com.backend.server.model.repository.user.UserRepository;
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
    private final UserRepository userRepository;

    public RentalHistoryListResponse getRentalHistoryList(RentalHistoryListRequest request, LoginUser loginUser) {
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("로그인 된 사용자를 DB 에서 찾을수 없습니다."));

        Specification<RentalHistory> spec = RentalHistorySpecification.filter(request, user);
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
