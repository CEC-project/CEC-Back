package com.backend.server.model.repository.equipment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.backend.server.model.entity.equipment.EquipmentModel;

@Repository
public interface EquipmentModelRepository extends JpaRepository<EquipmentModel, Long>, JpaSpecificationExecutor<EquipmentModel> {
    boolean existsByName(String name);
    boolean existsByEnglishCode(String englishCode);

    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByEnglishCodeAndIdNot(String englishCode, Long id);
}
