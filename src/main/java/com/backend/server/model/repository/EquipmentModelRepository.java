package com.backend.server.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.backend.server.model.entity.EquipmentModel;

@Repository
public interface EquipmentModelRepository extends JpaRepository<EquipmentModel, Long>, JpaSpecificationExecutor<EquipmentModel> {
    boolean existsByName(String name);
    boolean existsByEnglishCode(String englishCode);

    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByEnglishCodeAndIdNot(String englishCode, Long id);
}
