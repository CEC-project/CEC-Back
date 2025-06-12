package com.backend.server.model.repository.equipment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.server.model.entity.equipment.EquipmentModel;

import java.util.Optional;

@Repository
public interface EquipmentModelRepository extends JpaRepository<EquipmentModel, Long>, JpaSpecificationExecutor<EquipmentModel> {
    boolean existsByName(String name);
    boolean existsByEnglishCode(String englishCode);

    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByEnglishCodeAndIdNot(String englishCode, Long id);

    // 특정 englishCodePrefix로 시작하는 모델 중 가장 큰 modelGroupIndex를 가진 모델을 찾아 반환
    @Query(value = "SELECT em FROM EquipmentModel em " +
            "WHERE SUBSTRING(em.englishCode, 1, 3) = :englishCodePrefix " +
            "ORDER BY em.modelGroupIndex DESC")
    Optional<EquipmentModel> findTopByEnglishCodePrefixOrderByModelGroupIndexDesc(@Param("englishCodePrefix") String englishCodePrefix);
}
