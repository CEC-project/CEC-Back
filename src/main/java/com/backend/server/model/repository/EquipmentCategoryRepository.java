package com.backend.server.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.server.model.entity.EquipmentCategory;

public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, Long> {
    boolean existsByName(String name);
    boolean existsByEnglishCode(String englishCode);
    //SELECT EXISTS (
    //    SELECT 1
    //    FROM equipment_category
    //    WHERE name = '카메라'
    //      AND id <> 1
    //);
    //아래 메서드의 sql쿼리. 자기 자신을 제외하고 존재여부 파악. 업데이트 시 중복검사에 사용
    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByEnglishCodeAndIdNot(String englishCode, Long id);
}