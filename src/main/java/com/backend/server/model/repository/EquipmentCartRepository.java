package com.backend.server.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.backend.server.model.entity.EquipmentCart;

public interface EquipmentCartRepository extends JpaRepository<EquipmentCart, Long>{
    boolean existsByUserIdAndEquipmentId(Long userId, Long equipmentId);
    List<EquipmentCart> findByUserId(Long userId);
    void deleteByUserIdAndEquipmentId(Long userId, Long equipmentId);
}
