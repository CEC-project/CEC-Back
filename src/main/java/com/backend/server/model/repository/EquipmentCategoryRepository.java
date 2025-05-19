package com.backend.server.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.server.model.entity.EquipmentCategory;

public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, Long> {
    boolean existsByName(String name);
    boolean existsByEnglishCode(String englishCode);
}