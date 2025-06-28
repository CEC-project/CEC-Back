package com.backend.server.api.admin.equipment.service;

import com.backend.server.api.admin.equipment.dto.category.AdminEquipmentCountByCategoryResponse;
import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.entity.equipment.EquipmentCategory;
import com.backend.server.model.repository.equipment.EquipmentCategoryRepository;
import com.backend.server.api.admin.equipment.dto.category.AdminEquipmentCategoryCreateRequest;

import com.backend.server.model.repository.equipment.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.backend.server.model.entity.enums.Status.AVAILABLE;
import static com.backend.server.model.entity.enums.Status.BROKEN;


@Service
@RequiredArgsConstructor
@Transactional
public class AdminEquipmentCategoryService {

    private final EquipmentCategoryRepository categoryRepository;
    private final EquipmentRepository equipmentRepository;
    //중복검사
    public void checkExist(AdminEquipmentCategoryCreateRequest request){
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다.");
        }
        if (categoryRepository.existsByEnglishCode(request.getEnglishCode())) {
            throw new IllegalArgumentException("이미 존재하는 영문 코드입니다.");
        }
    }

    //업데이트 중복검사
    private void checkExistForUpdate(Long id, AdminEquipmentCategoryCreateRequest request) {
        if (categoryRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다.");
        }
        if (categoryRepository.existsByEnglishCodeAndIdNot(request.getEnglishCode(), id)) {
            throw new IllegalArgumentException("이미 존재하는 영문 코드입니다.");
        }
    }
    // 카테고리 생성
    public Long createCategory(AdminEquipmentCategoryCreateRequest request) {
        checkExist(request);
        EquipmentCategory savedCategory = categoryRepository.save(request.toEntity());
        return savedCategory.getId();
    }

    //카테고리 수정
    public Long updateCategory(Long id, AdminEquipmentCategoryCreateRequest request) {
        checkExistForUpdate(id, request);
        EquipmentCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다. id=" + id));

        category = category.toBuilder()
                .name(request.getName())
                .maxRentalCount(request.getMaxRentalCount())
                .englishCode(request.getEnglishCode())
                .build();

        EquipmentCategory savedCategory = categoryRepository.save(category);
        return savedCategory.getId();
    }

    //카테고리 삭제
    public Long deleteCategory(Long id) {
        EquipmentCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다. id=" + id));
        category.softDelete();
        categoryRepository.save(category);
        return id;
    }

    public List<AdminEquipmentCountByCategoryResponse> countAllCategoryWithEquipment() {
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


            return new AdminEquipmentCountByCategoryResponse(
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