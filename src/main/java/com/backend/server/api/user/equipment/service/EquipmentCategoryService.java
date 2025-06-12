package com.backend.server.api.user.equipment.service;

import com.backend.server.api.user.equipment.dto.category.EquipmentCategoryListResponse;
import com.backend.server.api.user.equipment.dto.category.EquipmentCategoryResponse;
import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.repository.equipment.EquipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.server.model.entity.equipment.EquipmentCategory;
import com.backend.server.model.repository.equipment.EquipmentCategoryRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.backend.server.model.entity.enums.Status.AVAILABLE;
import static com.backend.server.model.entity.enums.Status.BROKEN;

@Service
@RequiredArgsConstructor
public class EquipmentCategoryService{
    
    private final EquipmentCategoryRepository categoryRepository;
    private final EquipmentRepository equipmentRepository;
    
    @Transactional(readOnly = true)
    public List<EquipmentCategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(EquipmentCategoryResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EquipmentCategoryResponse getCategoryById(Long id) {
        EquipmentCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다. id=" + id));
        return new EquipmentCategoryResponse(category);
    }

    public List<EquipmentCategoryListResponse> countAllCategoryWithEquipment() {
        List<EquipmentCategory> categories = categoryRepository.findAll();
        List<Equipment> allEquipment = equipmentRepository.findAll();

        return categories.stream().map(category -> {
            List<Equipment> filtered = allEquipment.stream()
                    .filter(e -> e.getEquipmentCategory().getId().equals(category.getId()))
                    .toList();

            Integer total = filtered.size();
            Integer available = (int) filtered.stream()
                    .filter(e -> e.getStatus() == AVAILABLE)
                    .count();
            Integer broken = (int) filtered.stream()
                    .filter(e -> e.getStatus() == BROKEN)
                    .count();


            return new EquipmentCategoryListResponse(
                    category.getId(),
                    category.getName(), // 혹시 name 필드 없으면 category.getType() 같은 걸로 바꿔야 함
                    category.getEnglishCode(),
                    total,
                    available,
                    category.getMaxRentalCount(),
                    broken
            );
        }).toList();
    }


}
